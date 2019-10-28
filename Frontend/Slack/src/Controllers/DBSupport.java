package Controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.URISyntaxException;

public class DBSupport {

    public static HTTPResponse serverRequest(String url) throws URISyntaxException, IOException, InterruptedException {
        URL uri = new URL(url);
        HttpURLConnection con = (HttpURLConnection) uri.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");
        int status = con.getResponseCode();
        Reader streamReader = null;

        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }
        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        streamReader.close();
        in.close();
        con.disconnect();

        return new HTTPResponse(status, content.toString());
    }

    public static String handleErr(){
        System.out.println("Unable to handle the request, please check your connection, try again");
        return null;
    }

    public static HTTPResponse putWorkspace(String name){
      try{
          HTTPResponse response = serverRequest(ParamBuilder.createWorkspace(name));
          return response;
      }  catch(Exception e){
          return new HTTPResponse(406, handleErr());
      }
    }


    public static class HTTPResponse{
        public int code;
        public String response;

        HTTPResponse(int status, String content){
            code = status;
            response = content;
        }
    }
    private static class ParamBuilder{
        //This is the base url for our server. When we get a dedicated server for the app, we will want this changed
        private static String BASE_URL = "http://localhost:8080/";

        //Below are the Builders for the URL mappings
        //URLs are as follows:
        //      BASE_URL + CONTROLLER_MAPPING + / + REQUESTMAPPING + ?PARAM1_NAME=PARAM1
        //For 2+ params:
        //      BASE_URL + CONTROLLER_MAPPING + / + REQUESTMAPPING + ?PARAM1_NAME=PARAM1&PARAM2_NAME=PARAM2....

        public static String createWorkspace(String name){
            return BASE_URL+"workspace/add?name="+name;
        }

        public static String JoinWorkspace(String workspaceName, String username){
            return BASE_URL+"user/join?workspaceName="+workspaceName+"&name="+username;
        }
    }
}
