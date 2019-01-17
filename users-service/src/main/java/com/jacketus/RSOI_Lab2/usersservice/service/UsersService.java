package com.jacketus.RSOI_Lab2.usersservice.service;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.exception.UserNotFoundException;

import java.util.List;

public interface UsersService {
    User findUserById(Long id) throws UserNotFoundException;

    User findUserByLogin(String login) throws UserNotFoundException;

    List<User> getAllUsers();

    void createUser(User user);
}


