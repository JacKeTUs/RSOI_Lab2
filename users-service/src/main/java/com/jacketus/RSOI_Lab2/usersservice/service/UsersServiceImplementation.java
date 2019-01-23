package com.jacketus.RSOI_Lab2.usersservice.service;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.exception.UserNotFoundException;
import com.jacketus.RSOI_Lab2.usersservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImplementation implements UsersService{
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImplementation(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException{
        return usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findUserByLogin(String login) throws UserNotFoundException{
        User user = usersRepository.findByLogin(login);
        if (user == null)
            throw  new UserNotFoundException(login);
        return user;
    }

    @Override
    public void incBuyNum(Long id) throws UserNotFoundException{
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setBuy_num(user.getBuy_num() + 1);
        usersRepository.save(user);
    }

    @Override
    public User createUser(User user) {
        return usersRepository.save(user);
    }
}
