package Models;

import Controllers.DBSupport;
/**
 * Model for the Workspace within the front end. Will contain the data and methods required of the workspace
 * @Author Dylan Mrzlak
 */
public class Workspace {

    private String name;
    private int id;

    /**
     * Basic Constructor. Since the id is not known until it is in the DB, we can only instantiate the name
     * @param name
     * @Author Dylan Mrzlak
     */
    public Workspace(String name){
       this.name = name;
       this.id = -1;
    }

    /**
     * Create a workspace and call for the DBSUpport to request it put into the DB
     * @Author Dylan Mrzlak
     */
    public static DBSupport.HTTPResponse createWorkspace(String name){
        DBSupport.HTTPResponse res = DBSupport.putWorkspace(name);
        return res;
    }

    public static DBSupport.HTTPResponse joinWorkspace(String name, String username){
        DBSupport.HTTPResponse res = DBSupport.joinWorkspace(name, username);
        return res;
    }


    String getName(){
        return name;
    }
    int getwId(){
        return id;
    }
}
