package com.slack.server.messages;

import com.slack.server.channel.Channel;
import com.slack.server.channel.ChannelRepository;
import com.slack.server.user.User;
import com.slack.server.user.UserRepository;
import com.slack.server.workspace.Workspace;
import com.slack.server.workspace.WorkspaceRepository;
import com.slack.server.workspaceXRef.WorkspaceXRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/message") // This means URL's start with /demo (after Application path)
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private WorkspaceXRefRepository workspaceXRefRepository;



    @RequestMapping(path="/directMessage")
    @ResponseBody
    ResponseEntity directMessage(@RequestParam String senderName, @RequestParam String rid, @RequestParam String message){
        User sender = userRepository.findByName(senderName);
        if(sender == null)
            return new ResponseEntity("User Sender Not Found!!!", HttpStatus.NOT_FOUND);
        User recipient = userRepository.findByName(rid);
        if(recipient == null)
            return new ResponseEntity("User recipient Not Found!!!", HttpStatus.NOT_FOUND);
        Message m = new Message();
        m.setSenderId(sender.getId());
        m.setRecipientID(recipient.getId());
        m.setContent(message);
        m.setwId(null);
        m.setcID(null);
        m.setPinned(false);
        messageRepository.save(m);
        return new ResponseEntity(m, HttpStatus.OK);
    }

    @RequestMapping(path="/channelMessage")
    @ResponseBody
    ResponseEntity channelMessage(@RequestParam String senderName, @RequestParam String workSpaceName,
                                  @RequestParam String channelName, @RequestParam String message){
        User sender = userRepository.findByName(senderName);
        if(sender == null)
            return new ResponseEntity("User Sender Not Found!!!", HttpStatus.NOT_FOUND);
        Workspace workspace = workspaceRepository.findbyName(workSpaceName);
        if(workspace == null)
            return new ResponseEntity("Workspace Not Found!!", HttpStatus.NOT_FOUND);
        Channel channel = channelRepository.find(workspace.getId(),channelName);
        if(channel == null)
            return new ResponseEntity("Channel Not Found :(", HttpStatus.NOT_FOUND);
        Message m = new Message();
        m.setSenderId(sender.getId());
        m.setRecipientID(null);
        m.setContent(message);
        m.setwId(workspace.getId());
        m.setcID(channel.getId());
        m.setPinned(false);
        messageRepository.save(m);
        return new ResponseEntity(m, HttpStatus.OK);
    }

    /**
     * sets a messages pinned status to true
     * @param messageID
     * @return error if ID doesn't match any existing messages.
     * @author Joseph Hudson
     */
    @RequestMapping(path="/pinMessage")
    @ResponseBody
    ResponseEntity pinMessage(@RequestParam Integer messageID){
        if(!messageRepository.existsByID(messageID))
            return new ResponseEntity("No message with that ID is found", HttpStatus.NOT_ACCEPTABLE);
        Message m = messageRepository.getByID(messageID);
        m.setPinned(true);
        messageRepository.save(m);

        return new ResponseEntity(m, HttpStatus.OK);
    }

}

