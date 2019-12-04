package Controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.ArrayList;

/**
 * This is our Data Provider for the frontend app. THis will cal to the backend controllers via HTTPRequests.
 * The idea goes:
 *      User --> InputController --> Model --> DBSupport --> HTTPRequest --> ModelController --> DB
 *      User <-- InputController <-- Model <-- DBSupport <-- HTTPRequest <-- ModelController <--
 *   (Interface)
 * @Author Dylan Mrzlak
 */

public class DBSupport {


    /**
     * Connect to the backend via HTTPRequest. The controllers on the backend have specific URL mappings,
     * so we can use those to proc the backend to do its work
     *
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

    //Prints an error for stating the a request couldn't finish for whatever reason.
    // Helps keep the app from breaking completely
    public static String handleErr() {
        System.out.println("Unable to handle the request, please check your connection, try again");
        return null;
    }

    /**
     * Creates a request to the backend to make a Workspace
     *
     * @param name
     * @return
     * @Author Dylan Mrzlak
     */
    public static HTTPResponse putWorkspace(String name) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.createWorkspace(name));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }


    /**
     * Builds the request to join a workspace
     *
     * @param workspaceName
     * @param name
     * @return
     */
    public static HTTPResponse joinWorkspace(String workspaceName, String name) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.joinWorkspace(workspaceName, name));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }


    /**
     * Builds a request to create a user
     *
     * @param name
     * @param password
     * @return
     */
    public static HTTPResponse createUser(String name, String password) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.createUser(name, password));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Sets a message as pinned
     *
     * @param id
     * @return
     * @Author Joseph Hudson
     */
    public static HTTPResponse pinMessage(Integer id) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.pinMessage(id));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Builds a request to send a message
     *
     * @param senderName
     * @param workspaceName
     * @param channelName
     * @param message
     * @return
     */
    public static HTTPResponse sendMessage(String senderName, String workspaceName, String channelName, String message) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.sendMessage(senderName, workspaceName, channelName, message));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public static HTTPResponse getText(String name) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getText(name));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     *
     * @param fileName
     * @param content
     * @return
     */
    public static HTTPResponse sendText(String fileName, String content) {
        try {
            String url = ParamBuilder.sendText(fileName, content);
            if (url.length() > 2048) {
                url = url.substring(0, 2048);
                System.out.print("File too long. Truncating Message");
            }
            HTTPResponse response = serverRequest(url);
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Builds a request to create a channel
     *
     * @param workspaceName
     * @param name
     * @return
     */
    public static HTTPResponse createChannel(String workspaceName, String name) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.addNewChannel(workspaceName, name));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Builds a request to send a DM
     *
     * @param senderName
     * @param receiver
     * @param message
     * @return
     */
    public static HTTPResponse sendDirectMessage(String senderName, String receiver, String message) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.sendDirectMessage(senderName, receiver, message));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Builds a request to view users in a workspace
     *
     * @param workspaceName
     * @return
     */
    public static HTTPResponse viewUsers(String workspaceName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getUsersInWorkspace(workspaceName));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Get all the mentions within a channel for a given user
     *
     * @param username
     * @param workspaceName
     * @param channelName
     * @return Status code of the HTTP call and a response string (either a JSON or a string)
     */
    public static HTTPResponse viewMentions(String username, String workspaceName, String channelName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.viewMentions(username, workspaceName, channelName));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Get all the messages within a workspace
     *
     * @param workspaceName
     * @return Status code of the HTTP call and a response string (either a JSON or a string)
     * The Json is a list of messages all grouped by channel
     */
    public static HTTPResponse getAllMessages(String workspaceName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getAllMessages(workspaceName));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * searcher workspace
     *
     * @param workspaceName
     * @return Status code of the HTTP call and a json list of the workspaces
     */
    public static HTTPResponse searchWorkspace(String workspaceName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.searchWorkspace(workspaceName));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Get all the pinned messages within a channel
     *
     * @param workspaceName
     * @param channelName
     * @return Status code of the HTTP call and a response string (either a JSON or a string)
     * The Json is a list of messages all grouped by channel
     */
    public static HTTPResponse getPinnedMessages(String workspaceName, String channelName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getPinnedMessages(workspaceName, channelName));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * searcher workspace
     *
     * @param userName
     * @return Status code of the HTTP call and a json list of the workspaces
     */
    public static HTTPResponse searchUser(String userName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.searchUser(userName));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Builds a request to get a channel name
     *
     * @param cId
     * @return
     */
    public static HTTPResponse getChannelName(int cId) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getChannelName(cId));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Builds a request to get a user by id
     *
     * @param senderId
     * @return
     */
    public static HTTPResponse getUserNameByID(Integer senderId) {
        //Now that users may be removed completely the chance that the user may no longer be in the repo we were
        // searching in, so now we also check against historic data that gets updated when a user removes themselves

        // As this is only used for logging (no actual modifying the DB)
        // we are safe to update to look at historical data as well
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getUserNameById(senderId));
            if(response.code == 404) response = serverRequest(ParamBuilder.getHistoricUserNameById(senderId));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Builds a request to sign in a user
     *
     * @param username
     * @param password
     * @return
     */
    public static HTTPResponse signin(String username, String password) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.signin(username, password));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse unpinMessage(int id) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.unpinMessage(id));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse changeRole(String workspace, String username, int newRole) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.changeRole(workspace, username, newRole));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse getRole(String workspace, String username) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getRole(workspace, username));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse viewFriends(int uId) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.viewFriends(uId));
            return response;
        }   catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse addFriend(String name, String fName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.addFriend(name, fName));
            return response;
        }   catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse getWorkspaceByName(String name) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getWorkspaceByName(name));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse getChannelByName(String workspaceName, String name) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getChannelByName(workspaceName, name));
            return response;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse switchWorkspace(String workspaceName, int userId) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.switchWorkspace(workspaceName, userId));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse SwitchChannel(String workspaceName, String channelName, Integer userId) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.switchChannel(workspaceName, channelName, userId));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse deleteFriend(String uName, String fName) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.deleteFriend(uName, fName));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }
    
    public static HTTPResponse clearUser(String username) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.clearUser(username));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }
    
    public static HTTPResponse createAppointment(String name, String description, String time, int user) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.createAppointment(name, description, time, user));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }
    public static HTTPResponse sendInvite(int aId,  ArrayList<Integer> userIds){
        try {
            HTTPResponse res = serverRequest(ParamBuilder.sendInvite(aId, userIds));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }
    
    public static HTTPResponse getAppointments(String user, boolean accepted,  boolean pending){
        try {
            HTTPResponse res = serverRequest(ParamBuilder.getAppointments(user, accepted, pending));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }
        
    public static HTTPResponse deleteAppointment(String user,  int aId){
        try {
            HTTPResponse res = serverRequest(ParamBuilder.deleteAppointment(user, aId));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }
        
    public static HTTPResponse respondAppointment(String user,  int aId,  boolean accept){
        try {
            HTTPResponse res = serverRequest(ParamBuilder.respondAppointment(user, aId, accept));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse getUserIdByName(String username) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.getUserIdByName(username));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse setStatus(String uName, String status) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.setStatus(uName, status));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse deleteStatus(String uName) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.deleteStatus(uName));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse viewStatus(String fName){
        try {
            HTTPResponse res = serverRequest(ParamBuilder.viewStatus(fName));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse kickUser(String wName, String uName, String toKick) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.kickUser(wName, uName, toKick));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse unkickUser(String wName, String uName, String toKick) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.unkickUser(wName, uName, toKick));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static HTTPResponse viewFavorites(int wID, int cID, int uID) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.viewFavorites(wID,cID,uID));
            return res;
        } catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }
	
    public static HTTPResponse favoriteMessage(int uID,int mID) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.favoriteMessage(uID,mID));
            return res;
        } catch (Exception e) {
			  return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse muteUser(String uName, String mName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.muteUser(uName, mName));
            return response;
        }   catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse unmuteUser(String uName, String mName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.unmuteUser(uName, mName));
            return response;
        }   catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse setDesc(String workspaceName, String channelName, String desc) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.setDesc(workspaceName, channelName, desc));
            return response;
        }   catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    public static HTTPResponse unFavoriteMessage(int uID,int mID) {
        try {
            HTTPResponse res = serverRequest(ParamBuilder.unfavoriteMessage(uID,mID));
            return res;
        } catch (Exception e) {
			  return new HTTPResponse(406, handleErr());
        }
    }
    public static HTTPResponse getDesc(String workspaceName, String channelName) {
        try {
            HTTPResponse response = serverRequest(ParamBuilder.getDesc(workspaceName, channelName));
            return response;
        }   catch (Exception e) {
            return new HTTPResponse(406, handleErr());
        }
    }

    /**
     * Model for the HTPPResponse rebuilding, that way the objects can handle the data themselve
     *
     * @author Dylan Mrzlak
     */
    public static class HTTPResponse {
        public int code;
        public String response;

        HTTPResponse(int status, String content) {
            code = status;
            response = content;
        }
    }

    /**
     * Static class to build our URL's to Strings.
     * Makes it a lot better to send it out to here, rather than build them in other methods
     */
    private static class ParamBuilder {
        //This is the base url for our server. When we get a dedicated server for the app, we will want this changed
        private static String BASE_URL = "http://localhost:8080/";

        //Below are the Builders for the URL mappings
        //URLs are as follows:
        //      BASE_URL + CONTROLLER_MAPPING + / + REQUESTMAPPING + ?PARAM1_NAME=PARAM1
        //For 2+ params:
        //      BASE_URL + CONTROLLER_MAPPING + / + REQUESTMAPPING + ?PARAM1_NAME=PARAM1&PARAM2_NAME=PARAM2....


        public static String viewFavorites(int wID, int cID, int uID){
            return BASE_URL + "/channel/getFavoriteMessages?wID=" + wID + "&cID=" + cID + "&uID="+ uID;
        }
        public static String favoriteMessage(int uID, int mID){
            return BASE_URL + "/favorite/favoriteMessage?uId="+uID+"&mID="+mID+"&favorite="+true;
        }
        public static String unfavoriteMessage(int uID, int mID){
            return BASE_URL + "/favorite/favoriteMessage?uId="+uID+"&mID="+mID+"&favorite="+false;
        }

        public static String sendDirectMessage(String sender, String reciever, String message) {
            return BASE_URL + "message/directMessage?senderName=" + sender + "&recieverName=" + reciever + "&message=" + message;
        }

        public static String sendMessage(String sender, String workspace, String channelName, String message) {
            return BASE_URL + "message/channelMessage?senderName=" + sender + "&workSpaceName=" + workspace + "&channelName=" + channelName + "&message=" + message;
        }

        public static String sendText(String name, String content) {
            return BASE_URL + "textfile/send?name=" + name + "&content=" + content;
        }

        public static String getText(String name) {
            return BASE_URL + "textfile/download?name=" + name;
        }

        public static String createWorkspace(String name) {
            return BASE_URL + "workspace/add?name=" + name;
        }

        public static String joinWorkspace(String workspaceName, String username) {
            return BASE_URL + "user/join?workspaceName=" + workspaceName + "&name=" + username;
        }

        public static String createUser(String name, String password) {
            return BASE_URL + "user/add?username=" + name + "&password=" + password;
        }

        public static String pinMessage(int mId) {
            return BASE_URL + "message/pinMessage?messageID=" + mId;
        }

        public static String unpinMessage(int mId) {
            return BASE_URL + "message/unpinMessage?messageID=" + mId;
        }

        public static String getUsersInWorkspace(String workspaceName) {
            return BASE_URL+"workspace/getUsers?name="+workspaceName;
        }

        public static String searchWorkspace(String workspaceName) {
            return BASE_URL + "workspace/search?name=" + workspaceName;
        }

        public static String searchUser(String userName) {
            return BASE_URL + "user/search?name=" + userName;
        }

        public static String addNewChannel(String workspaceName, String name) {
            return BASE_URL + "channel/add?workspaceName=" + workspaceName + "&name=" + name;
        }

        public static String viewMentions(String username, String workspaceName, String channelName) {
            return BASE_URL + "channel/viewMentions?username=" + username +
                    "&workspaceName=" + workspaceName +
                    "&channelName=" + channelName;
        }

        public static String getAllMessages(String workspaceName) {
            return BASE_URL + "workspace/getAllMessages?workspaceName=" + workspaceName;
        }

        public static String getChannelName(int cId) {
            return BASE_URL + "channel/getName?cId=" + cId;
        }

        public static String getUserNameById(Integer senderId) {
            return BASE_URL + "user/getUsername?senderId=" + senderId;
        }

        public static String getHistoricUserNameById(Integer senderId) {
            return BASE_URL + "user/getHistoricUsername?senderId=" + senderId;
        }

        public static String changeRole(String workspace, String username, int rId){
            return BASE_URL+"workspace/changeRole?workspace=" + workspace + "&username=" + username + "&rId=" + rId;
        }

        public static String getRole(String workspace, String username){
            return BASE_URL+"workspace/getRole?workspace=" + workspace + "&username=" + username;
        }

        public static String signin(String username, String password) {
            return BASE_URL+"user/login?username="+username+"&password="+password;
        }

        public static String viewFriends(int uId) {
            return BASE_URL+"user/viewFriends?uId="+uId;
        }

        public static String addFriend(String uName, String fName) {
            return BASE_URL+"user/addFriend?uName="+uName+"&fName="+fName;
        }

        public static String getWorkspaceByName(String name) {
            return BASE_URL+"workspace/get?name="+name;
        }

        public static String getChannelByName(String workspaceName, String name) {
            return BASE_URL+"channel/get?workspaceName="+workspaceName+"&name="+name;
        }

        public static String switchWorkspace(String workspaceName, int userId) {
            return BASE_URL+"workspace/switch?workspaceName="+workspaceName+"&userId="+userId;
        }

        public static String switchChannel(String workspaceName, String channelName, Integer userId) {
            return BASE_URL+"channel/switch?workspaceName="+workspaceName+"&channelName="+channelName+"&userId="+userId;
        }

        public static String deleteFriend(String uName, String fName) {
            return BASE_URL+"user/removeFriend?uName="+uName+"&fName="+fName;
        }

        public static String getPinnedMessages(String workspaceName, String channelName) {
            return BASE_URL + "channel/getPinnedMessages?workspaceName=" + workspaceName + "&channelName=" + channelName;
        }

        public static String setStatus(String uName, String status) {
            return BASE_URL + "user/setStatus?uName=" + uName + "&status=" + status;
        }

        public static String deleteStatus(String uName) {
            return BASE_URL + "user/deleteStatus?uName=" + uName;
        }

        public static String viewStatus(String fName) {
            return BASE_URL + "user/viewStatus?fName=" + fName;
        }

        public static String kickUser(String workspaceName, String uName, String toKick) {
            return BASE_URL + "user/kickUser?workspaceName=" + workspaceName + "&uName=" + uName + "&toKick=" + toKick;
        }

        public static String unkickUser(String workspaceName, String uName, String toUnkick) {
            return BASE_URL + "user/unkickUser?workspaceName=" + workspaceName + "&uName=" + uName + "&toUnkick=" + toUnkick;
		}
		
        public static String muteUser(String uName, String mName) {
            return BASE_URL+"user/muteUser?uName="+uName+"&mName="+mName;
        }

        public static String unmuteUser(String uName, String mName) {
            return BASE_URL+"user/unmuteUser?uName="+uName+"&mName="+mName;
        }

        public static String setDesc(String workspaceName, String channelName, String desc) {
            return BASE_URL+"channel/setDescription?workspaceName="+workspaceName+"&channelName="+channelName+"&desc="+desc;
        }

        public static String getDesc(String workspaceName, String channelName) {
            return BASE_URL+"channel/getDescription?workspaceName="+workspaceName+"&channelName="+channelName;
        }

        public static String clearUser(String name){
            return BASE_URL+"user/clear?username="+name;
        }

        public static String createAppointment(String name, String description, String time, int user){
          return BASE_URL + "appointment/create?name=" + name + "&description=" + description + "&timeString=" +
                  time + "&user=" + user;
        }
        
        public static String sendInvite(int aId,  ArrayList<Integer> userIds){
            String idString = "&userIdList=";
            for(int id : userIds){
                idString+=id + ",";
            }
            idString = idString.substring(0, idString.length()-1);
            return BASE_URL + "appointment/sendInvite?aId="+aId+idString;
        }
        
        public static String getAppointments(String user, boolean accepted,  boolean pending){
          return BASE_URL + "appointment/get?user="+user+"&accepted="+accepted+"&pending="+pending;
        }
        
        public static String deleteAppointment(String user,  int aId){
          return BASE_URL + "appointment/delete?user="+user+"&aId="+aId;
        }
        
        public static String respondAppointment(String user,  int aId,  boolean accept){
          return BASE_URL + "appointment/respond?user="+user+"&aId="+aId+"&accept="+accept;
        }

        public static String getUserIdByName(String username) {
            return BASE_URL+"user/getId?username="+username;
        }
	}
}
