package Controllers;

public class DBSupport {


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
            return "";
        }
    }
}
