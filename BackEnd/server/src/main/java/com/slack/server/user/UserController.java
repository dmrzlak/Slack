package com.slack.server.user;

import com.slack.server.appointment.Appointment;
import com.slack.server.appointment.AppointmentRepository;
import com.slack.server.appointmentXRef.AppointmentXRefRepository;
import com.slack.server.workspace.Workspace;
import com.slack.server.workspace.WorkspaceRepository;
import com.slack.server.workspaceXRef.WorkspaceXRef;
import com.slack.server.workspaceXRef.WorkspaceXRefRepository;
import com.slack.server.userXRef.UserXRef;
import com.slack.server.userXRef.UserXRefRepository;
//import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Parameter;

/**
 * Controller for the Messages in the server
 * We set a Mapping to a specified value, and all http requests that use that
 *      (BASE_URL + /mapping)
 * Come here. This class handles all login for the given section
 */
@Controller    // This means that this class is a Controller
@RequestMapping(path="/user") // This means URL's start with /demo (after Application path)
public class UserController {

    /**
     * Repo section
     * Autowired gives the controller access to the specified repositories (tables)
     */
    @Autowired
    private UserRepository uRepo;
    @Autowired
    private UserHistoricalRepository uHistoryRepo;

    @Autowired
    private WorkspaceRepository wRepo;

    @Autowired
    private WorkspaceXRefRepository wXRefRepo;

    @Autowired
    private UserXRefRepository uXRefRepo;

    @Autowired
    private AppointmentXRefRepository aXRefRepo;

    @Autowired
    private AppointmentRepository aRepo;

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
     * Login for a user. Get the user by a name, check that the passwords are equal. If so, we're good
     * @param username
     * @param password
     * @return
     */
    @GetMapping(path="/login")
    public @ResponseBody ResponseEntity login(@RequestParam String username, @RequestParam String password){
        if(!uRepo.existsByName(username)) return new ResponseEntity("No User found", HttpStatus.NOT_FOUND);
        User u = uRepo.findByName(username);
        if(password.equals(u.getPassword())) return new ResponseEntity(u, HttpStatus.OK);
        return new ResponseEntity("Incorrect Password", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(path="/search")
    public @ResponseBody ResponseEntity searchUser(@RequestParam String name){
        Iterable<User> list;
        if(name.equals("-1")){
            list = uRepo.findAll();
        }else {
            name = "%" + name + "%";
            list =  uRepo.searchUser(name);
        }
        return new ResponseEntity(list, HttpStatus.OK);
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


    /** get a user via name
     */
    @GetMapping(path="/get")
    public @ResponseBody ResponseEntity getUser(@RequestParam String name){
        if(uRepo.existsByName(name)){
            User u = uRepo.findByName(name);
            return new ResponseEntity(u, HttpStatus.OK);
        }
        return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);

    }
 /** get a user via name
     */
    @GetMapping(path="/getId")
    public @ResponseBody ResponseEntity getUserById(@RequestParam String username){
        User u = uRepo.findByName(username);
        if(u== null)
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity(u.getId(), HttpStatus.OK);
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
        User u = uRepo.findByName(name);
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


    /**
     * Find a user by id, and return its username
     * @param senderId
     * @return
     */
    @GetMapping(path = "/getUsername")
    public @ResponseBody ResponseEntity getUserNameById(Integer senderId) {
        if(uRepo.existsById(senderId)) {
            User user = uRepo.findByID(senderId);
            return new ResponseEntity(user.getName(), HttpStatus.OK);
        }
        return new ResponseEntity("User Does Not Exist", HttpStatus.NOT_FOUND);

    }

    /**
     * Find a user by id, and return its username
     * @param senderId
     * @return
     */
    @GetMapping(path = "/getHistoricUsername")
    public @ResponseBody ResponseEntity getHistoricUserNameById(Integer senderId) {
        if(uHistoryRepo.existsById(senderId)) {
            User user = uRepo.findByID(senderId);
            return new ResponseEntity(user.getName(), HttpStatus.OK);
        }
        return new ResponseEntity("User Does Not Exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/viewFriends")
    public @ResponseBody ResponseEntity viewFriends(@RequestParam int uId) {
        Iterable<String> list = uRepo.viewFriends(uId);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping(path = "/addFriend")
    public @ResponseBody ResponseEntity addFriend(@RequestParam String uName, @RequestParam String fName){
        User u = uRepo.findByName(uName);
        User f = uRepo.findByName(fName);
        if(f == null) return new  ResponseEntity("User not found", HttpStatus.NOT_FOUND);

        if(uXRefRepo.exists(f.getId(), u.getId())) return new ResponseEntity("User already a friend", HttpStatus.NOT_ACCEPTABLE);

        //Create the XREF and put it in DB
        UserXRef x = new UserXRef();
        x.setfId(f.getId());
        x.setuId(u.getId());
        uXRefRepo.save(x);

        UserXRef x1 = new UserXRef();
        x1.setuId(f.getId());
        x1.setfId(u.getId());
        uXRefRepo.save(x1);

        return new ResponseEntity(f, HttpStatus.OK);
    }

    @GetMapping(path = "/removeFriend")
    public @ResponseBody ResponseEntity removeFriend(@RequestParam String uName, @RequestParam String fName) {
        User u = uRepo.findByName(uName);
        User f = uRepo.findByName(fName);
        if(f == null) return new  ResponseEntity("User not found", HttpStatus.NOT_FOUND);

        if(!uXRefRepo.exists(f.getId(), u.getId())) return new ResponseEntity("User isn't your real friend", HttpStatus.NOT_ACCEPTABLE);

        uXRefRepo.delete(f.getId(), u.getId());
        uXRefRepo.delete(u.getId(), f.getId());

        return new ResponseEntity(f, HttpStatus.OK);
    }


    @GetMapping(path = "/clear")
    public @ResponseBody ResponseEntity clearUser(@RequestParam String username){
        User u = uRepo.findByName(username);
        if(u == null) return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        UserHistory u1 = new UserHistory(u);
        uHistoryRepo.save(u1);
        wXRefRepo.removeByUserId(u.getId());
        uXRefRepo.removeByUserId(u.getId());
        aXRefRepo.removeByUserId(u.getId());
        aXRefRepo.removeByOwnerId(u.getId());
        aRepo.removeByOwnerId(u.getId());
        uRepo.delete(u);
        return new ResponseEntity("User deleted, sorry to see you go", HttpStatus.OK);
    }
}
