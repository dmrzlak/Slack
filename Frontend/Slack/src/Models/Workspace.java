package Models;

import Controllers.DBSupport;

public class Workspace {

    private String name;
    private int id;

    /**
     * Basic Constructor. Since the id is not known until it is in the DB, we can only instantiate the name
     * @param name
     */
    public Workspace(String name){
       this.name = name;
       this.id = -1;
    }

    public static boolean createWorkspace(String name){
        Workspace w = new Workspace(name);
        DBSupport.HTTPResponse result = DBSupport.putWorkspace(name);
        return result.code < 300;
    }

    String getName(){
        return name;
    }
    int getwId(){
        return id;
    }
}
