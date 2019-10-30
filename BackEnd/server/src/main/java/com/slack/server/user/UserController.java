package com.slack.server.user;

import com.slack.server.workspace.Workspace;
import com.slack.server.workspace.WorkspaceRepository;
import com.slack.server.workspaceXRef.WorkspaceXRef;
import com.slack.server.workspaceXRef.WorkspaceXRefRepository;
//import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/user") // This means URL's start with /demo (after Application path)
public class UserController {

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private WorkspaceRepository wRepo;

    @Autowired
    private WorkspaceXRefRepository wXRefRepo;


    /**
     * Create a user for the DB and put them into the table
     * @param username
     * @param password
     * @return
     * @author Dylan Mrzlak
     */
    @GetMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity createUser(@RequestParam String username, @RequestParam String password){
        if(uRepo.existsByName(username)) return new ResponseEntity("Username is taken", HttpStatus.NOT_ACCEPTABLE);
        User u = new User();
        u.setName(username);
        u.setPassword(password);
        uRepo.save(u);
        return new ResponseEntity(u, HttpStatus.OK);
    }

    /**
     * Gets all the users in the DB
     * @return
     * @author Dylan
     */
    @GetMapping(path="")
    public @ResponseBody ResponseEntity getAllUsers() {
        // This returns a JSON or XML with the workspaces
        Iterable<User> list = uRepo.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping(path="/get")
    public @ResponseBody ResponseEntity getUser(@RequestParam String name){
        if(uRepo.existsByName(name)){
            User u = uRepo.findbyName(name);
            return new ResponseEntity(u, HttpStatus.OK);
        }
        return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);

    }

    /**
     * When accessed, will add a user to a workspace. In the workspaceXRef table, if a user and a workspace are in the
     * same row, then that user is in the workspace
     * @param workspaceName
     * @param name
     * @return
     * @author Dylan Mrzlak
     */
    @GetMapping(path="/join")
    public @ResponseBody ResponseEntity joinWorkspace(@RequestParam String workspaceName, @RequestParam String name){
        //Get Workspace, we will use its ID later
        Workspace w = wRepo.findbyName(workspaceName);
        if(w == null) return new ResponseEntity("Workspace not found", HttpStatus.NOT_FOUND);
        //Get the user, we will use their ID later
        User u = uRepo.findbyName(name);
        if(u == null) return new  ResponseEntity("User not found", HttpStatus.NOT_FOUND);

        //Chack that the user isn't already in the workspace
        if(wXRefRepo.exists(w.getId(), u.getId())) return new ResponseEntity("User already in Workspace", HttpStatus.NOT_ACCEPTABLE);

        //Create the XREF and put it in DB
        WorkspaceXRef x = new WorkspaceXRef();
        x.setwId(w.getId());
        x.setuId(u.getId());
        wXRefRepo.save(x);

        //Return OK status (200) and workspace
        return new ResponseEntity(w, HttpStatus.OK);
    }
}
