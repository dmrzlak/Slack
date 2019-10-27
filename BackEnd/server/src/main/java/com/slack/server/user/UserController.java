package com.slack.server.user;

import com.slack.server.workspace.Workspace;
import com.slack.server.workspace.WorkspaceRepository;
import com.slack.server.workspaceXRef.WorkspaceXRef;
import com.slack.server.workspaceXRef.WorkspaceXRefRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody Pair<Integer, String> createUser(@RequestParam String username, @RequestParam String password){
        if(uRepo.existsByName(username)) return new Pair<>(400, "Username: " + username + " is taken");
        User u = new User();
        u.setName(username);
        u.setPassword(password);
        uRepo.save(u);
        return new Pair<>(200, "User " + username + " saved");
    }

    @GetMapping(path="")
    public @ResponseBody Iterable<User> getAllChannels() {
        // This returns a JSON or XML with the workspaces
        return uRepo.findAll();
    }

    @GetMapping(path="/get")
    public @ResponseBody Pair<Integer, User> getChannel(@RequestParam String name){
        return  (uRepo.existsByName(name) ?
                new Pair<>(200, uRepo.findbyName(name))
                : new Pair<>(404, null));
    }

    @GetMapping(path="/join")
    public @ResponseBody Pair<Integer, String> joinWorkspace(@RequestParam String workspaceName, @RequestParam String name){
        Workspace w = wRepo.findbyName(workspaceName);
        if(w == null) return new Pair<>(404, "Workspace" + workspaceName +" does not exists");
        User u = uRepo.findbyName(name);
        if(u == null) return new Pair<>(404, "User" + name +" does not exists");
        if(wXRefRepo.exists(w.getId(), u.getId())) return new Pair<>(400, "User" + name +" is already in Workspace " + workspaceName);
        WorkspaceXRef x = new WorkspaceXRef();
        x.setwId(w.getId());
        x.setuId(u.getId());
        wXRefRepo.save(x);
        return new Pair<>(200, "User" + name +" has joined Workspace " + workspaceName);
    }
}
