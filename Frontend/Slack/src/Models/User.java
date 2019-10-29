package Models;

import Controllers.DBSupport;

public class User {

    private String name;
    private String password;

    public User(String uName, String uPassword){
        name = uName;
        password = uPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String username) {
        password = username;
    }

    /**
     * Create a workspace and call for the DBSUpport to request it put into the DB
     * @Author Dylan Mrzlak
     */
    public static boolean createWorkspace(String workspaceName, String name){
        DBSupport.HTTPResponse result = DBSupport.joinWorkspace(workspaceName, name);
        return result.code < 300;
    }
}
