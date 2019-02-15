package com.jacketus.RSOI_Lab2.usersservice.service;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UsersService {
    User findUserById(Long id) throws UserNotFoundException;

    User findUserByLogin(String login) throws UserNotFoundException;

    void incBuyNum(Long id) throws UserNotFoundException;

    List<User> getAllUsers();

    User createUser(User user);

    User putUser(User user) throws UserNotFoundException;

    ResponseEntity check_health();
}


