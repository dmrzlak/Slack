package com.slack.server.favorites;


import com.slack.server.channel.Channel;
import com.slack.server.messages.Message;
import com.slack.server.messages.MessageRepository;
import com.slack.server.user.UserRepository;
import com.slack.server.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/favorite")
public class FavoriteController {


    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(path="/favoriteMessage")
    @ResponseBody
    ResponseEntity favoriteMessage(@RequestParam Integer userID,@RequestParam Integer messageID, boolean favorite){
        //if favorite favorite(Add to favorite repo) the message if exists.
        //else delete from XRef if exists
        //Return response of a string for all paths
        if(!favorite){
            if(favoriteRepository.exists(userID, messageID)) {
                favoriteRepository.deleteByIds(userID, messageID);
                return new ResponseEntity("Successfully unfavoritted message!", HttpStatus.OK);
            }
            return new ResponseEntity("Favorited message not found.", HttpStatus.NOT_FOUND);

        }
        Favorite f = new Favorite();
        f.setMessageID(messageID);
        f.setUserID(userID);
        favoriteRepository.save(f);
        return new ResponseEntity("Successfully favoritted message", HttpStatus.OK);
    }


}
