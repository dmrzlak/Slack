package com.slack.server.workspaceXRef;

import javax.persistence.*;


/**
 * Model for the WorkspaceXRef Table. Essentially this is what the table will contain
 * If a row exists in this table the User with uID belongs to the workspace with wId
 * @Author Dylan Mrzlak
 */
@Entity
public class WorkspaceXRef {
    //We will use this table to represent a user being a part of a workspace.
    //If a user's ID exists in this table with a workspace's ID, then that user is in that worksapce

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer wId;
    private Integer uId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getuId() {
        return uId;
    }

    public int getwId() {
        return wId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }
}
