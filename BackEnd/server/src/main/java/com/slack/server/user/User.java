package com.slack.server.user;
import com.slack.server.channel.Channel;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

/**
 * Model for the User Table. Essentially this is what the table will contain
 * @Author Dylan Mrzlak
 */
@Entity // This tells Hibernate to make a table out of this class
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    //Role id; 1 mute, 2 user, 3 mod, 4 admin

    private String name;

    private String password;

    private String status;

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

    public String getPassword(){ return password;}

    public void setPassword(String password){this.password = password; }

    public void setStatus(String status){this.status = status; }

    public String getStatus(){return status; }

}
