package com.externalapicalling.Controller;

import com.externalapicalling.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping
    public String getUser(@RequestBody String prompt){
        return userService.getUser(prompt);
    }
}
