package com.jacketus.RSOI_Lab4.authservice.controllers;

import com.jacketus.RSOI_Lab4.authservice.entities.Role;
import com.jacketus.RSOI_Lab4.authservice.entities.User;
import com.jacketus.RSOI_Lab4.authservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private UserService us;

    @GetMapping(value = "/")
    public String index(){

        //us.save(new User("user", "pass", new ArrayList<Role>(), true));
        return "Hello world";

    }

    @GetMapping(value = "/private")
    public String privateArea(){
        return "Private area";
    }

}
