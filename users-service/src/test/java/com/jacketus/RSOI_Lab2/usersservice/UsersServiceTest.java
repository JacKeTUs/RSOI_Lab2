package com.jacketus.RSOI_Lab2.usersservice;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.exception.UserNotFoundException;
import com.jacketus.RSOI_Lab2.usersservice.repository.UsersRepository;
import com.jacketus.RSOI_Lab2.usersservice.service.UsersService;
import com.jacketus.RSOI_Lab2.usersservice.service.UsersServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class UsersServiceTest {
    private UsersService usersService;

    @Mock
    UsersRepository usersRepository;

    @Before
    public void setUp(){
        initMocks(this);
        usersService = new UsersServiceImplementation(usersRepository);
    }

    @Test
    public void shouldReturnAllUsers(){
        List<User> users= new ArrayList<>();
        User user= new User();

        user.setName("Name");
        user.setLogin("Login");

        given(usersRepository.findAll()).willReturn(users);
        List<User> usersReturned = usersService.getAllUsers();
        assertThat(usersReturned, is(users));

    }

    @Test
    public void shouldCreateUser(){
        User user = new User();

        user.setName("Name");
        user.setLogin("Login");

        given(usersRepository.save(user)).willReturn(user);
        assertThat(usersRepository.save(user), is(user));
    }

    @Test
    public void shouldFindUserById() throws UserNotFoundException {
        User user = new User();

        user.setName("Name");
        user.setLogin("Login");

        given(usersRepository.save(user)).willReturn(user);
        given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));

        usersService.createUser(user);
        assertThat(usersService.findUserById(user.getId()), is(user));

        assertEquals("Login", user.getLogin());
        assertEquals("Name", user.getName());
    }

    @Test
    public void shouldFindUserByLogin() throws UserNotFoundException {
        User user = new User();

        user.setName("Name");
        user.setLogin("Login");

        given(usersRepository.save(user)).willReturn(user);

        given(usersRepository.findByLogin("Login")).willReturn(user);
        given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));

        usersService.createUser(user);
        assertThat(usersService.findUserByLogin("Login"), is(user));
    }

    @Test
    public void shouldIncBuyNum() throws UserNotFoundException {
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");

        given(usersRepository.save(user)).willReturn(user);
        given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));
        User saved = usersRepository.save(user);

        Long id = saved.getId();
        usersService.incBuyNum(id);

        assertEquals(1, usersService.findUserById(id).getBuy_num());
    }

    @Test
    public void shouldHash(){
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");

        try {
            given(usersRepository.save(user)).willReturn(user);
            given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));
            User saved = usersRepository.save(user);


            assertNotNull(saved.hashCode());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldToString(){
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");

        String s = "User{id=null, login='Login', name='Name', buy_num='0'}";

        try {
            given(usersRepository.save(user)).willReturn(user);
            given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));
            User saved = usersRepository.save(user);

            System.out.println(saved.toString());
            assertEquals(s, saved.toString());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldEquals(){
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");

        try {
            given(usersRepository.save(user)).willReturn(user);
            given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));
            User saved = usersRepository.save(user);


            boolean eq = user.equals(saved);
            assertEquals(true, eq);
        }
        catch (Exception ex){
            fail();
        }
    }
}
