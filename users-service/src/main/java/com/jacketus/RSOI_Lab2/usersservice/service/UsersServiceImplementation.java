package com.jacketus.RSOI_Lab2.usersservice.service;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.exception.UserNotFoundException;
import com.jacketus.RSOI_Lab2.usersservice.repository.UsersRepository;
import org.codehaus.jackson.map.util.JSONPObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            throw new UserNotFoundException(login);
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


    @Override
    public ResponseEntity check_health() {
        JSONObject json1 = new JSONObject();
        try {
            json1.put("info", "Users M Service");
            json1.put("version", 0.4);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(json1.toString());
    }

    @Override
    public User putUser(User newUser) throws UserNotFoundException {
        return usersRepository.findById(newUser.getId()).map(User -> {
            if (newUser.getName() != null && !newUser.getName().isEmpty())
                User.setName(newUser.getName());
            if (newUser.getLogin() != null && !newUser.getLogin().isEmpty())
                User.setLogin(newUser.getLogin());
            if (newUser.getDescription() != null && !newUser.getDescription().isEmpty())
                User.setDescription(newUser.getDescription());
            /*if (newUser.getType() != null && !newUser.getType().isEmpty())
                User.setDescription(newUser.getDescription());*/
            return usersRepository.save(User);
        }).orElseThrow(() -> new UserNotFoundException(newUser.getId()));
    }

}
