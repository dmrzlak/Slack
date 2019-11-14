package com.slack.server.workspace;

import com.slack.server.user.User;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

/**
 * Model for the Workspace Table. Essentially this is what the table will contain
 * @Author Dylan Mrzlak
 */

// This tells Hibernate to make a table out of this class
@Entity
public class Workspace {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String name;

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

}
