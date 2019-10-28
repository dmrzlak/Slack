package Models;

import Controllers.DBSupport;

public class User {

    /**
     * Create a workspace and call for the DBSUpport to request it put into the DB
     * @Author Dylan Mrzlak
     */
    public static boolean createWorkspace(String workspaceName, String name){
        DBSupport.HTTPResponse result = DBSupport.joinWorkspace(workspaceName, name);
        return result.code < 300;
    }
}
