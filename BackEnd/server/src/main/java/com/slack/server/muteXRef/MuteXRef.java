package com.slack.server.muteXRef;

import javax.persistence.*;


/**
 * Model for the MuteXRef
 * If a row exists in this table the User with uID has muted the user with mID
 * @Author Logan Garrett
 */
@Entity
public class MuteXRef {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer mId;
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

    public int getmId() {
        return mId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public void setmId(int fId) {
        this.mId = fId;
    }
}
