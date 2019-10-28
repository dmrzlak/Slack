package Controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This is our Data Provider for the frontend app. THis will cal to the backend controllers via HTTPRequests.
 * The idea goes:
 *      User --> InputController --> Model --> DBSupport --> HTTPRequest --> ModelController --> DB
 * @Author Dylan Mrzlak
 */
public class DBSupport {


    /**
     * Connect to the backend via HTTPRequest. The controllers on the backend have specific URL mappings,
     *      so we can use those to proc the backend to do its work
     * @param url
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     * @Author Dylan Mrzlak
     */
    public static HTTPResponse serverRequest(String url) throws URISyntaxException, IOException, InterruptedException {
        //Build the URL out of the built string
        URL uri = new URL(url);

        //Open a connection to uri, we will soon be able to make the actual request
        //Also instantiate the rest of the request
        HttpURLConnection con = (HttpURLConnection) uri.openConnection();
        //By only using GET we can simplify calls on either side. The framework we're using, spring boot doesn't care
        //musch about the actual request method
        con.setRequestMethod("GET");
        //We want a json to be returned in the event htat we get an object returned from the controller.
        //They don't really send Objects, but rather a string style of encoding called a json.
        //These are really simple enough to understand when looking at the JSONString
        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");

        //We want to know if we did good, or if Big Backend is mad at us
        int status = con.getResponseCode();
        Reader streamReader = null;
        //When the status is 300 or over, that means the server is not happy to some degree about the request
        //Statuses usually go as follow
        //100 ok-ish?
        //200 Yay we're all good
        //300 Not so great
        //400 You messed up
        //500 Mr. Stark, I don't feel so good
        if (status > 299) {
            //We want to be able to read the repsonse as an Error
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            //We want to be able to actually read the response
            streamReader = new InputStreamReader(con.getInputStream());
        }
        //Read the response, what ever it is
        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        streamReader.close();
        in.close();
        con.disconnect();
        //Return the response rebuilt into the HTTPResponse built here
        return new HTTPResponse(status, content.toString());
    }

    //Prints an error for stating the a request couldn't finish for whatever reason. Helps keep the app from being pissy
    public static String handleErr(){
        System.out.println("Unable to handle the request, please check your connection, try again");
        return null;
    }

    /**
     * Creates a request to the backend to make a Workspace
     * @param name
     * @return
     * @Author Dylan Mrzlak
     */
    public static HTTPResponse putWorkspace(String name){
      try{
          HTTPResponse response = serverRequest(ParamBuilder.createWorkspace(name));
          return response;
      }  catch(Exception e){
          return new HTTPResponse(406, handleErr());
      }
    }

    /**
     * Model for the HTPPResponse rebuilding, that way the objects can handle the data themselve
     * @author Dylan Mrzlak
     */
    public static class HTTPResponse{
        public int code;
        public String response;

        HTTPResponse(int status, String content){
            code = status;
            response = content;
        }
    }

    /**
     * Staatic class to build our URL's to Strings.
     * Makes it a lot better to send it out to here, rather than build them in other methods
     */
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
