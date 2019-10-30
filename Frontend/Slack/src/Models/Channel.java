package Models;

public class Channel {
    private Integer id;

    private Integer wId;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getwId() {
        return wId;
    }

    public void setwId(Integer wId) {
        this.wId = wId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Channel(String name, int wID){
        this.name = name;
        this.id = -1;
        this.wID = wID;
    }
}