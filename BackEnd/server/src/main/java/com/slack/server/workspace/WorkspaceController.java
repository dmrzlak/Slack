package com.slack.server.workspace;


import com.slack.server.messages.Message;
import com.slack.server.messages.MessageRepository;
import com.slack.server.user.User;
import com.slack.server.user.UserRepository;
import com.slack.server.workspaceXRef.WorkspaceXRef;
import com.slack.server.workspaceXRef.WorkspaceXRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Controller for the Messages in the server
 * We set a Mapping to a specified value, and all http requests that use that
 *      (BASE_URL + /mapping)
 * Come here. This class handles all login for the given section
 */
@Controller    // This means that this class is a Controller
@RequestMapping(path="/workspace") // This means URL's start with /demo (after Application path)
public class WorkspaceController {

    /**
     * Repo section
     * Autowired gives the controller access to the specified repositories (tables)
     */
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository mRepo;

    @Autowired
    private WorkspaceXRefRepository xRefRepository;

    /**
     * Create and put a Workspaceinto the table
     * @param name
     * @return
     * @author Dylan Mrzlak
     */
    @GetMapping(path="/add")
    public @ResponseBody ResponseEntity addNewWorkspace (@RequestParam String name) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        if(workspaceRepository.existsByName(name)) return new ResponseEntity("Workspace name already taken", HttpStatus.NOT_ACCEPTABLE);
        Workspace n = new Workspace();
        n.setName(name);
        workspaceRepository.save(n);
        return new ResponseEntity(n, HttpStatus.OK);
    }

    /**
     * Get all workspaces in DB
     * @return
     * @Author Dylan Mrzlak
     */
    @GetMapping(path="")
    public @ResponseBody ResponseEntity getAllWorkspaces() {
        // This returns a JSON or XML with the workspaces
        Iterable<Workspace> list = workspaceRepository.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    /**
     * Get a workspace by name from the DB
     * @param name
     * @return
     * @Author Dylan Mrzlak
     */
    @GetMapping(path="/get")
    public @ResponseBody ResponseEntity getWorkspaceByName(@RequestParam String name){
        if(workspaceRepository.existsByName(name)) return new ResponseEntity(workspaceRepository.findbyName(name), HttpStatus.OK);
        return new ResponseEntity("Workspace does not exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/search")
    public @ResponseBody ResponseEntity searchWorkspace(@RequestParam String name){
        Iterable<Workspace> list;
        if(name.equals("-1")){
            list = workspaceRepository.findAll();
        }else {
            name = "%" + name + "%";
            list =  workspaceRepository.searchWorkspace(name);
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    /**
     * Get all the users in a given workspace
     * @param name
     * @return
     */
    @GetMapping(path="/getUsers")
    public @ResponseBody  ResponseEntity getUsersInWorkspace(@RequestParam String name){
        Iterable<String> list = userRepository.findUsers(name);
        return new ResponseEntity(list, HttpStatus.OK);
    }


    @GetMapping(path="/getAllMessages")
    public @ResponseBody ResponseEntity getAllMessages(String workspaceName) {
        Workspace w = workspaceRepository.findbyName(workspaceName);
        Iterable<Message> list = mRepo.getAllMessagesByWorkspace(w.getId());
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping(path="/switch")
    public @ResponseBody ResponseEntity switchWorkspace(String workspaceName, int userId) {
        Workspace w = workspaceRepository.findbyName(workspaceName);
        if (w == null) return new ResponseEntity("Workspace not found", HttpStatus.NOT_FOUND);
        WorkspaceXRef x = xRefRepository.find(w.getId(), userId);
        boolean inWorkspace = x != null && x.getrId() > -1;
        if (inWorkspace) return new ResponseEntity(w, HttpStatus.OK);
        return new ResponseEntity("Not in workspace, you must join it first: " + w.getName(), HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(path="/changeRole")
    public @ResponseBody ResponseEntity changeRole(String workspace, String username, int rId) {
        if(! userRepository.existsByName(username))
            return new ResponseEntity("User: " + username + " Not found", HttpStatus.NOT_FOUND);
        User u = userRepository.findByName(username);
        if(!workspaceRepository.existsByName(workspace))
            return new ResponseEntity("User: " + username + " Not found", HttpStatus.NOT_FOUND);
        Workspace w =  workspaceRepository.findbyName(workspace);
        if(!xRefRepository.exists(w.getId(), u.getId()))
            return new ResponseEntity("User: " + username + " Not in workspace", HttpStatus.NOT_ACCEPTABLE);
        WorkspaceXRef x = xRefRepository.find(w.getId(), u.getId());
        x.setrId(rId);
        xRefRepository.save(x);
        return new ResponseEntity(x, HttpStatus.OK);
    }

    @GetMapping(path="/getRole")
    public @ResponseBody ResponseEntity getRole(String workspace, String username) {
        if(! userRepository.existsByName(username))
            return new ResponseEntity("User: " + username + " Not found", HttpStatus.NOT_FOUND);
        User u = userRepository.findByName(username);
        if(!workspaceRepository.existsByName(workspace))
            return new ResponseEntity("User: " + username + " Not found", HttpStatus.NOT_FOUND);
        Workspace w =  workspaceRepository.findbyName(workspace);
        if(!xRefRepository.exists(w.getId(), u.getId()))
            return new ResponseEntity("User: " + username + " Not in workspace", HttpStatus.NOT_ACCEPTABLE);
        WorkspaceXRef x = xRefRepository.find(w.getId(), u.getId());
        x.getrId();
        return new ResponseEntity(x.getrId(), HttpStatus.OK);
    }
}
