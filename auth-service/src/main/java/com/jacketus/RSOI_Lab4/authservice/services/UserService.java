package com.jacketus.RSOI_Lab4.authservice.services;

import com.jacketus.RSOI_Lab4.authservice.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jacketus.RSOI_Lab4.authservice.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user){
        System.out.println("enter");
        System.out.println(user.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("setpasswd");
        repo.save(user);
        System.out.println("repo save");
    }

}
