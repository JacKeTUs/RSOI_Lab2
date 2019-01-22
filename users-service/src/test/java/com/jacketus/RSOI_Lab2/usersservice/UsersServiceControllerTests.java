package com.jacketus.RSOI_Lab2.usersservice;


import com.jacketus.RSOI_Lab2.usersservice.controller.UsersServiceController;
import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import com.jacketus.RSOI_Lab2.usersservice.repository.UsersRepository;
import com.jacketus.RSOI_Lab2.usersservice.service.UsersService;
import com.jacketus.RSOI_Lab2.usersservice.service.UsersServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersServiceControllerTests {

    //@Autowired
    private MockMvc mvc;

    @MockBean
    private UsersServiceController usersServiceController;

    @Mock
    UsersService usersService;

    @Mock
    UsersRepository usersRepository;

    @Before
    public void setUp(){
        initMocks(this);

        usersService = new UsersServiceImplementation(usersRepository);
        usersServiceController = new UsersServiceController(usersService);
        mvc = MockMvcBuilders.standaloneSetup(usersServiceController).build();
    }

    @Test
    public void addUser() throws Exception {
        List<User> users = new ArrayList<>();

        User user = new User();
        user.setLogin("Login");
        user.setName("Name");
        users.add(user);

        mvc.perform(post("/users")
                .contentType("application/json")
                .content("{\"login\":\"Login\", \"name\":\"Name\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsers() throws Exception {
        List<User> users = new ArrayList<>();

        User user = new User();
        user.setLogin("Login");
        user.setName("Name");
        users.add(user);
        users.add(user);
        users.add(user);

        given(usersServiceController.getAllUsers()).willReturn(users);
        mvc.perform(get("/users")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].name", is(user.getName())))
                .andExpect(jsonPath("[1].name", is(user.getName())));
    }

    @Test
    public void getUserByID() throws Exception {
        List<User> users = new ArrayList<>();

        User user = new User();
        user.setLogin("Login");
        user.setName("Name");
        users.add(user);

        given(usersRepository.findById(1L)).willReturn(Optional.of(user));
        mvc.perform(get("/users/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(user.getName())));
    }

    @Test
    public void buySong() throws Exception {
        List<User> users = new ArrayList<>();

        User user = new User();
        user.setLogin("Login");
        user.setName("Name");
        users.add(user);

        given(usersRepository.findById(1L)).willReturn(Optional.of(user));
        mvc.perform(post("/users/1/buy")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mvc.perform(get("/users/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("buy_num", is(1)));
    }
}
