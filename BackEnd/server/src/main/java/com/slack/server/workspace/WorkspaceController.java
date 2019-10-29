package com.slack.server.workspace;

import com.slack.server.user.User;
import com.slack.server.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/workspace") // This means URL's start with /demo (after Application path)
public class WorkspaceController {
    // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create and put a Workspaceinto the table
     * @param name
     * @return
     * @author Dylan Mrzlak
     */
    @GetMapping(path="/add") // Map ONLY POST Requests
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

    @GetMapping(path="/getUsers")
    public @ResponseBody  ResponseEntity getUsersInWorkspace(@RequestParam String name){
        Iterable<String> list = userRepository.findUsers(name);
        return new ResponseEntity(list, HttpStatus.OK);
    }


}
