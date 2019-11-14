package com.slack.server.userXRef;

import javax.persistence.*;


/**
 * Model for the UserXRef
 * If a row exists in this table the User with uID is friends with the user with fId
 * @Author Logan Garrett
 */
@Entity
public class UserXRef {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer fId;
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

    public int getfId() {
        return fId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
