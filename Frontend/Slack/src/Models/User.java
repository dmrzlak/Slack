package Models;

import Controllers.DBSupport;
/**
 * Model for the user, holds the same data that the server would and contains the static calls to the dbsupport
 */
public class User {

    private String name;
    private String password;
    private Integer id;

    public User(String uName, String uPassword){
        name = uName;
        password = uPassword;
        id = -1;
    }

    public User(String uName, String uPassword, Integer uId){
        name = uName;
        password = uPassword;
        id = uId;
    }

    /**
     * Calls DBSupport and returns the response
     * @param username
     * @param password
     * @return
     */
    public static DBSupport.HTTPResponse signIn(String username, String password) {
        DBSupport.HTTPResponse res = DBSupport.signin(username, password);
        return res;
    }

    /**
     * Calls DBSupport and returns the response
     * @param senderId
     * @return
     */
    public static DBSupport.HTTPResponse getUserNameByID(Integer senderId) {
        DBSupport.HTTPResponse res = DBSupport.getUserNameByID(senderId);
        return res;
    }

    public static DBSupport.HTTPResponse clearUser(String username) {
        DBSupport.HTTPResponse res = DBSupport.clearUser(username);
        return res;
    }

    public static DBSupport.HTTPResponse getUserIdByName(String username) {
        DBSupport.HTTPResponse res = DBSupport.getUserIdByName(username);
        return res;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String username) {
        password = username;
    }


    public static DBSupport.HTTPResponse searchUser(String name){
        DBSupport.HTTPResponse res = DBSupport.searchUser(name);
        return res;
    }

    public static DBSupport.HTTPResponse createUser(String name, String password){
        DBSupport.HTTPResponse res = DBSupport.createUser(name, password);
        return res;
    }

    public static DBSupport.HTTPResponse viewFriends(int uId) {
        DBSupport.HTTPResponse res = DBSupport.viewFriends(uId);
        return res;
    }

    public static DBSupport.HTTPResponse addFriend(String uName, String fName) {
        DBSupport.HTTPResponse res = DBSupport.addFriend(uName, fName);
        return res;
    }

    public static DBSupport.HTTPResponse deleteFriend(String uName, String fName) {
        DBSupport.HTTPResponse res = DBSupport.deleteFriend(uName, fName);
        return res;
    }

    public static DBSupport.HTTPResponse getPinnedMessages(String workspaceName, String channelName) {
        DBSupport.HTTPResponse res = DBSupport.getPinnedMessages(workspaceName, channelName);
        return res;
    }

    public static DBSupport.HTTPResponse muteUser(String uName, String mName) {
        DBSupport.HTTPResponse res = DBSupport.muteUser(uName, mName);
        return res;
    }

    public static DBSupport.HTTPResponse unmuteUser(String uName, String mName) {
        DBSupport.HTTPResponse res = DBSupport.unmuteUser(uName, mName);
        return res;
    }

}
