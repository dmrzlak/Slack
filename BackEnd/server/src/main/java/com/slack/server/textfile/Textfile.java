package com.slack.server.textfile;

import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class Textfile {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Integer id;

    private String name;

    private String content;


    public Textfile(Integer id,String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
    public Textfile(String name, String content){
        this.id = -1;
        this.name = name;
        this.content = content;
    }
    public Textfile(){
        this.id = -1;
        this.name = null;
        this.content = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return content;
    }

    public void setName(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
