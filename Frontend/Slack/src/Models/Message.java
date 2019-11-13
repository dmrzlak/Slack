package Models;

import Controllers.DBSupport;

/**
 * Model for the Message Table. Essentially this is what the table will contain
 * @Author Dylan Mrzlak
 */
public class Message {

    private Integer id;

    private Integer senderId;

    private Integer wId;

    private Integer cID;

    private Integer recipientID;

    private String content;

    private Boolean pinned;

    public Message(Integer id, Integer senderId, Integer wId, Integer cID, Integer recipientID, String content, Boolean pinned) {
        this.id = id;
        this.senderId = senderId;
        this.wId = wId;
        this.cID = cID;
        this.recipientID = recipientID;
        this.content = content;
        this.pinned = pinned;
    }

    //dm
    public Message(Integer id, Integer senderId, Integer recipientID, String content) {
        this.id = id;
        this.senderId = senderId;
        this.recipientID = recipientID;
        this.content = content;
    }

    //public message
    public Message(Integer id, Integer senderId, Integer wId, Integer cID, String content, Boolean pinned) {
        this.id = id;
        this.senderId = senderId;
        this.wId = wId;
        this.cID = cID;
        this.content = content;
        this.pinned = pinned;
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

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public void setwId(Integer wId) {
        this.wId = wId;
    }

    public Integer getcID() {
        return cID;
    }

    public void setcID(Integer cID) {
        this.cID = cID;
    }

    public Integer getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(Integer recipientID) {
        this.recipientID = recipientID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    /**
     * Calls DBSupport and returns the response
     * @param senderName
     * @param workspaceName
     * @param channelName
     * @param message
     * @return
     */
    public static DBSupport.HTTPResponse sendMessage(String senderName, String workspaceName, String channelName, String message){
            DBSupport.HTTPResponse res = DBSupport.sendMessage(senderName, workspaceName, channelName, message);
            return res;

    }

    /**
     * Calls DBSupport and returns the response
     * @param senderName
     * @param receiver
     * @param message
     * @return
     */
    public static DBSupport.HTTPResponse sendDirectMessage(String senderName, String receiver, String message){
        DBSupport.HTTPResponse res = DBSupport.sendDirectMessage(senderName, receiver, message);
        return res;
    }

    public static DBSupport.HTTPResponse getAllMessages(String name) {
        DBSupport.HTTPResponse res = DBSupport.getAllMessages(name);
        return res;
    }

    public static DBSupport.HTTPResponse getPinnedMessages(String workspaceName, String channelName) {
        DBSupport.HTTPResponse res = DBSupport.getPinnedMessages(workspaceName, channelName);
        return res;
    }

}
