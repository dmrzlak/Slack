package Models;

import Controllers.DBSupport;

/**
 * Model for the channel, holds the same data that the server would and contains the static calls to the dbsupport
 */
public class Channel {
    private Integer id;

    private Integer wId;

    private String name;

    /**
     * Calls DBSupport and returns the response
     * @param workspaceName
     * @param name
     * @return
     */
    public static DBSupport.HTTPResponse createChannel(String workspaceName, String name) {
        DBSupport.HTTPResponse res = DBSupport.createChannel(workspaceName, name);
        return res;
    }

    /**
     * Calls DBSupport and returns the response
     * @param userName
     * @param workspaceName
     * @param channelName
     * @return
     */
    public static DBSupport.HTTPResponse viewMentions(String userName, String workspaceName, String channelName) {
        DBSupport.HTTPResponse res = DBSupport.viewMentions(userName, workspaceName, channelName);
        return res;
    }

    /**
     * Calls DBSupport and returns the response
     * @param cId
     * @return
     */
    public static DBSupport.HTTPResponse getChannelName(int cId) {
        DBSupport.HTTPResponse res = DBSupport.getChannelName(cId);
        return res;
    }

    public static DBSupport.HTTPResponse getChannelByName(String workspaceName, String name) {
        DBSupport.HTTPResponse res = DBSupport.getChannelByName(workspaceName, name);
        return res;
    }

    public static DBSupport.HTTPResponse Switch(String workspaceName, String channelName, Integer userId) {
        DBSupport.HTTPResponse res = DBSupport.SwitchChannel(workspaceName, channelName, userId);
        return res;
    }

    public static DBSupport.HTTPResponse setDesc(String workspaceName, String channelName, String desc) {
        DBSupport.HTTPResponse res = DBSupport.setDesc(workspaceName, channelName, desc);
        return res;
    }

    public static DBSupport.HTTPResponse getDesc(String workspaceName, String channelName) {
        DBSupport.HTTPResponse res = DBSupport.getDesc(workspaceName, channelName);
        return res;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getwId() {
        return wId;
    }

    public void setwId(Integer wId) {
        this.wId = wId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Channel(String name, int wID){
        this.name = name;
        this.id = -1;
        this.wId = wID;
    }

    public Channel(String name, int wID, int id){
        this.name = name;
        this.id = id;
        this.wId = wID;

    }


}