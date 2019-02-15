package com.jacketus.RSOI_Lab2.usersservice.controller;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.exception.UserNotFoundException;
import com.jacketus.RSOI_Lab2.usersservice.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UsersServiceController {
    private UsersService usersService;
    private Logger logger;

    @Autowired
    public UsersServiceController(UsersService usersService){
        this.usersService = usersService;
        logger = LoggerFactory.getLogger(UsersServiceController.class);
    }

     // Добавление пользователя
    @PostMapping(value = "/users")
    public void createUser(@RequestBody User user){
        logger.info("[POST] /users", user);
        usersService.createUser(user);
    }

    // Получение списка пользователей
    @GetMapping(value = "/users")
    public List<User> getAllUsers(){
        logger.info("[GET] /users");
        return usersService.getAllUsers();
    }

    // Получение информации о пользователе
    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable Long id) throws UserNotFoundException {
        logger.info("[GET] /users/" + id);
        return usersService.findUserById(id);
    }

    // Покупка пользователем песни
    @PostMapping(value = "/users/{id}/buy")
    public void userBuySong(@PathVariable Long id) throws UserNotFoundException {
        logger.info("[POST] /users/" + id + "/buy");
        usersService.incBuyNum(id);
    }

    // Поиск по username
    @GetMapping(value = "/users/find")
    public User getUserByName(@RequestParam(value = "username") String username) throws UserNotFoundException {
        logger.info("[GET] /users/"+ username);
        return usersService.findUserByLogin(username);
    }

    @PutMapping(value="/users/edit")
    public void putUser(@RequestBody User user) throws UserNotFoundException {
        logger.info("[PUT] /users/edit");
        usersService.putUser(user);
    }

    @GetMapping(value = "/check_health")
    public ResponseEntity checkHealth(){
        logger.info("[GET] /check_health");

        return usersService.check_health();
    }

}
