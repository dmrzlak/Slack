package Models;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

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

    boolean createWorkspace(String name){
        Workspace w = new Workspace(name);

        return true;
    }

    String getName(){
        return name;
    }
    int getwId(){
        return id;
    }
}
