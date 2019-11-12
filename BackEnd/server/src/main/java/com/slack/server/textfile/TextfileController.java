package com.slack.server.textfile;


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
public class TextfileController {

    @Autowired
    private TextfileRepository tRepo;



    @GetMapping(path="/send")
    public @ResponseBody
    ResponseEntity sendText(@RequestParam String name, @RequestParam String content){
        if(tRepo.existsByName(name)) return new ResponseEntity("Filename is taken", HttpStatus.NOT_ACCEPTABLE);
        Textfile t = new Textfile();
        t.setName(name);
        t.setContent(content);
        tRepo.save(t);
        return new ResponseEntity(t, HttpStatus.OK);
    }
    @GetMapping(path="/download")
    public @ResponseBody
    ResponseEntity sendText(@RequestParam String name){
        if(!tRepo.existsByName(name)) return new ResponseEntity("File does not exist", HttpStatus.NOT_FOUND);
        Textfile t = tRepo.findByName(name);
        return new ResponseEntity(t, HttpStatus.OK);
    }
}
