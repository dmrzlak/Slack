package com.slack.server.messages;


import org.springframework.lang.Nullable;

import javax.persistence.*;
/**
 * Model for the Message Table. Essentially this is what the table will contain
 * @Author Dylan Mrzlak
 */
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private Integer senderId;

    @Nullable
    private Integer wId;

    @Nullable

    private Integer cId;

    @Nullable
    private Integer recipientID;

    private String content;

    private Boolean pinned;

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
        return cId;
    }

    public void setcID(Integer cID) {
        this.cId = cID;
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
}
