package com.slack.server.user;
import com.slack.server.channel.Channel;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

/**
 * Model for the User Table. Essentially this is what the table will contain
 * @Author Dylan Mrzlak
 */
@Entity // This tells Hibernate to make a table out of this class
public class UserHistory {

    @Id
    private Integer id;

    //Role id; 1 mute, 2 user, 3 mod, 4 admin

    private String name;

    private String password;

    public UserHistory() {
    }

    public UserHistory(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public UserHistory(User u) {
        this.id = u.getId();
        this.name = u.getName();
        this.password = u.getPassword();
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

    public String getPassword(){ return password;};

    public void setPassword(String password){this.password = password; }

}
