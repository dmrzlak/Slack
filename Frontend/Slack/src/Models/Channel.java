package Models;

import Controllers.DBSupport;

public class Channel {
    private Integer id;

    private Integer wId;

    private String name;

    public static DBSupport.HTTPResponse createChannel(String workspaceName, String name) {
        DBSupport.HTTPResponse res = DBSupport.createChannel(workspaceName, name);
        return res;
    }

    public static DBSupport.HTTPResponse viewMentions(String userName, String workspaceName, String channelName) {
        DBSupport.HTTPResponse res = DBSupport.viewMentions(userName, workspaceName, channelName);
        return res;
    }

    public static DBSupport.HTTPResponse getChannelName(int cId) {
        DBSupport.HTTPResponse res = DBSupport.getChannelName(cId);
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