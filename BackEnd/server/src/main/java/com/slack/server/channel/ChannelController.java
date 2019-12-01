package com.slack.server.channel;

import com.slack.server.favorites.Favorite;
import com.slack.server.favorites.FavoriteRepository;
import com.slack.server.messages.Message;
import com.slack.server.messages.MessageRepository;
import com.slack.server.user.User;
import com.slack.server.user.UserRepository;
import com.slack.server.workspace.Workspace;
import com.slack.server.workspace.WorkspaceRepository;
//import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for the Messages in the server
 * We set a Mapping to a specified value, and all http requests that use that
 *      (BASE_URL + /mapping)
 * Come here. This class handles all login for the given section
 */
@Controller    // This means that this class is a Controller
@RequestMapping(path="/channel") // This means URL's start with /demo (after Application path)
public class ChannelController {

    /**
     * Repo section
     * Autowired gives the controller access to the specified repositories (tables)
     */
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository mRepo;

    @Autowired
    private FavoriteRepository favoriteXRefRepo;

    /**
     * Create a channel for the DB and put them into the table
     * @param workspaceName
     * @param name
     * @return
     * @author Dylan Mrzlak
     */
    @GetMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity addNewChannel(@RequestParam String workspaceName, @RequestParam String name){
        Workspace w = workspaceRepository.findbyName(workspaceName);
        if(w == null) return new ResponseEntity("Workspace not found", HttpStatus.NOT_FOUND);
        if(channelRepository.exists(w.getId(), name)) return new ResponseEntity("Channel Already Exists", HttpStatus.NOT_ACCEPTABLE);
        Channel c = new Channel();
        c.setwId(w.getId());
        c.setName(name);
        channelRepository.save(c);
        return new ResponseEntity(c, HttpStatus.OK);
    }

    /**
     * Gets all the Channels in the DB
     * @return
     * @author Dylan
     */
    @GetMapping(path="")
    public @ResponseBody ResponseEntity getAllChannels() {
        // This returns a JSON or XML with the workspaces
        Iterable<Channel> list = channelRepository.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }


    /**
     * Gets a certain Channel in the DB
     * @param workspaceName
     * @param name
     * @return
     * @author Dylan
     */
    @GetMapping(path="/get")
    public @ResponseBody ResponseEntity getChannel(@RequestParam String workspaceName, @RequestParam String name){
        //Check that the workspace itself exists
        Workspace w = workspaceRepository.findbyName(workspaceName);
        if(w == null) return new ResponseEntity("Workspace not found", HttpStatus.NOT_FOUND);
        //Return the channel and HttpStatus.200 if it exists, or a 404 and a detail
        if(channelRepository.exists(w.getId(), name)){
            Channel c = channelRepository.find(w.getId(), name);
            return new ResponseEntity(c, HttpStatus.OK);
        }
        return new ResponseEntity("Channel Does Not Exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Get all the mentions for a user in a channel
     * @param username
     * @param workspaceName
     * @param channelName
     * @return
     */
    @GetMapping(path="/viewMentions")
    public @ResponseBody ResponseEntity viewMentions(String username, String workspaceName, String channelName) {
        Workspace w = workspaceRepository.findbyName(workspaceName);
        if(w == null) return new ResponseEntity("Workspace not found", HttpStatus.NOT_FOUND);
        Channel c = channelRepository.find(w.getId(), channelName);
        if(c == null) return new ResponseEntity("Channel not found", HttpStatus.NOT_FOUND);
        //Using SQL checking if a field is similar tp something using the keyword like
        //If we pass in a string and put the % wildcards around it, then we can check if the field contains the string
        //By doing that, we can search for only messages containing the username that we receive
        String query = "%" + username + "%";
        Iterable<Message> list = mRepo.getAllMessageContainsUName(query, w.getId(), c.getId());
        return new ResponseEntity(list, HttpStatus.OK);
    }


    /**
     * Return a channel's name after finding it by ID
     * @param cId
     * @return
     */
    @GetMapping(path="/getName")
    public @ResponseBody ResponseEntity getChannelName(int cId) {
        Channel c = channelRepository.findByID(cId);
        if(c == null) return new ResponseEntity("Channel not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity(c.getName(), HttpStatus.OK);

    }

    /**
     * @param workspaceName
     * @param channelName
     * @return
     * @Author Joseph Hudson
     */
    @GetMapping(path="/getPinnedMessages")
    public @ResponseBody ResponseEntity getPinnedMessages(String workspaceName, String channelName) {
        Workspace w = workspaceRepository.findbyName(workspaceName);
        Channel c = channelRepository.find(w.getId(), channelName);
        Iterable<Message> list = mRepo.getPinnedMessagesByChannel(w.getId(), c.getId());
        return new ResponseEntity(list, HttpStatus.OK);
    }

       @GetMapping(path="/getFavoriteMessages")
    public @ResponseBody ResponseEntity getFavoriteMessages(String workspaceName, String channelName,int uId) {
        Workspace w = workspaceRepository.findbyName(workspaceName);
        if(w == null){
            return new ResponseEntity("Workspace Not Found!!1",HttpStatus.NOT_FOUND);
        }

        Channel c = channelRepository.find(w.getId(), channelName);
           if(c == null){
               return new ResponseEntity("Channel Not Found!!1",HttpStatus.NOT_FOUND);
           }
           User u = userRepository.findByID(uId);
           if(u == null){
               return new ResponseEntity("User Not Found!!1",HttpStatus.NOT_FOUND);

           }
        Iterable<Message> list = mRepo.findFavorite(w.getId(), c.getId(),uId);
        return new ResponseEntity(list, HttpStatus.OK);
    }


    @RequestMapping(path="/favoriteMessage")
    @ResponseBody
    ResponseEntity favoriteMessage(@RequestParam Integer userID,@RequestParam Integer messageID, boolean favorite){
        //if favorite favorite(Add to favorite repo) the message if exists.
        //else delete from XRef if exists
        //Return response of a string for all paths
        if(!favorite){
            if(favoriteXRefRepo.exists(userID, messageID)) {
                favoriteXRefRepo.deleteByIds(userID, messageID);
                return new ResponseEntity("Successfully unfavoritted message!", HttpStatus.OK);
            }
            return new ResponseEntity("Favorited message not found.", HttpStatus.NOT_FOUND);

        }
        Favorite f = new Favorite();
        f.setMessageID(messageID);
        f.setUserID(userID);
        favoriteXRefRepo.save(f);
        return new ResponseEntity("Successfully favoritted message", HttpStatus.OK);
    }

}
