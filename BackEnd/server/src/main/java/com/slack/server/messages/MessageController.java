package com.slack.server.messages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/message") // This means URL's start with /demo (after Application path)
public class MessageController {

}
