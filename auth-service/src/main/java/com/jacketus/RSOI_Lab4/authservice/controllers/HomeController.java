package com.jacketus.RSOI_Lab4.authservice.controllers;

import com.jacketus.RSOI_Lab4.authservice.entities.Role;
import com.jacketus.RSOI_Lab4.authservice.entities.User;
import com.jacketus.RSOI_Lab4.authservice.services.UserService;

import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@RestController
public class HomeController {

    @Autowired
    private UserService us;

    @PostMapping(value = "/register_user")
    public void registerUser(@RequestBody String suser) {
        try {
            System.out.println(suser);
            JSONParser parser = new JSONParser();
            System.out.println(suser);
            JSONObject user =  (JSONObject) parser.parse(suser);/*
            boolean u = FALSE;
            try {
                user =
                u = TRUE;
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            //if (u) {
                us.save(new User((String)user.get("username"), (String)user.get("password"), new ArrayList<Role>(), true));
            //}
            System.out.println("llll");
        }
        catch (Exception e)
        {
            System.out.println("cant add user");
        }
    }
}
