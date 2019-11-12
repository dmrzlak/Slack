package Models;

import Controllers.DBSupport;

/**
 * Model for the Message Table. Essentially this is what the table will contain
 * @Author Dylan Mrzlak
 */
public class Textfile {

    private Integer id;

    private String name;

    private String content;


    public Message(Integer id,String name String content) {
        this.id = id;
        this.name = name;
        this.content = content;
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

    /**
     * Calls DBSupport and returns the response
     * @param message
     * @return
     */
    public static DBSupport.HTTPResponse sendText(String name, String message){
        DBSupport.HTTPResponse res = DBSupport.sendMessage(name, message);
        return res;

    }

}
