package com.jacketus.RSOI_Lab2.usersservice.controller;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.exception.UserNotFoundException;
import com.jacketus.RSOI_Lab2.usersservice.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UsersServiceController {
    private UsersService usersService;
    private Logger logger;

    @Autowired
    UsersServiceController(UsersService usersService){
        this.usersService = usersService;
        logger = LoggerFactory.getLogger(UsersServiceController.class);
    }

    @PostMapping(value = "/users")
    public void createUser(@RequestBody User user){
        usersService.createUser(user);
        logger.info("[POST] /users", user);
    }

    @GetMapping(value = "/users")
    public List<User> getAllUsers(){
        logger.info("[GET] /users");
        return usersService.getAllUsers();
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable Long id) throws UserNotFoundException {
        logger.info("[GET] /users/" + id);
        return usersService.findUserById(id);
    }

    @GetMapping(value = "/users/find")
    public User getUserByLogin(@RequestParam(value = "login") String login) throws UserNotFoundException {
        logger.info("[GET] /users/find ", login);
        return usersService.findUserByLogin(login);
    }
}
