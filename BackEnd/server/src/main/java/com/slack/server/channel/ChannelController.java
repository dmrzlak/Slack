package com.slack.server.channel;

import com.slack.server.workspace.Workspace;
import com.slack.server.workspace.WorkspaceRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/channel") // This means URL's start with /demo (after Application path)
public class ChannelController {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @GetMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody Pair<Integer, String> addNewChannel(@RequestParam String workspaceName, @RequestParam String name){
        Workspace w = workspaceRepository.findbyName(workspaceName);
        if(w == null) return new Pair<>(404, "Error Workspace " + workspaceName + "not found");
        if(channelRepository.exists(w.getId(), name))
            return new Pair<>(404, "Error channel " + workspaceName + "/" + name + " does not exists");
        Channel c = new Channel();
        c.setwId(w.getId());
        c.setName(name);
        channelRepository.save(c);
        return new Pair<>(200, "Channel " + w.getName() + "/" + name + " saved");
    }

    @GetMapping(path="")
    public @ResponseBody Iterable<Channel> getAllChannels() {
        // This returns a JSON or XML with the workspaces
        return channelRepository.findAll();
    }

    @GetMapping(path="/get")
    public @ResponseBody Pair<Integer, Channel> getChannel(@RequestParam String workspaceName, @RequestParam String name){
        Workspace w = workspaceRepository.findbyName(workspaceName);
        if(w == null) return new Pair<>(404, null);
        return  (channelRepository.exists(w.getId(), name) ?
                new Pair<>(200, channelRepository.find(w.getId(), name))
                : new Pair<>(404, null));

    }
}
