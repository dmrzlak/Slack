package com.slack.server.favorites;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer userID;

    private Integer messageID;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }
}
