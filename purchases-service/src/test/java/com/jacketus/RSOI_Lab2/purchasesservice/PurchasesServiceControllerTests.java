package com.jacketus.RSOI_Lab2.purchasesservice;

import com.jacketus.RSOI_Lab2.purchasesservice.controller.PurchasesServiceController;
import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import com.jacketus.RSOI_Lab2.purchasesservice.repository.PurchasesRepository;
import com.jacketus.RSOI_Lab2.purchasesservice.service.PurchasesService;
import com.jacketus.RSOI_Lab2.purchasesservice.service.PurchasesServiceImplementation;
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
public class PurchasesServiceControllerTests {

	//@Autowired
	private MockMvc mvc;

	@MockBean
	private PurchasesServiceController purchasesServiceController;

	@Mock
	PurchasesService purchasesService;

	@Mock
	PurchasesRepository purchasesRepository;

	@Before
	public void setUp(){
		initMocks(this);

        purchasesService = new PurchasesServiceImplementation(purchasesRepository);
        purchasesServiceController = new PurchasesServiceController(purchasesService);
		mvc = MockMvcBuilders.standaloneSetup(purchasesServiceController).build();
	}

    @Test
    public void addPurchase() throws Exception {

        mvc.perform(post("/purchases")
                .contentType("application/json")
                .content("{\"userID\":1, \"songID\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPurchase() throws Exception {

        Purchase purchase = new Purchase();
        purchase.setSongID(1L);
        purchase.setUserID(1L);

        given(purchasesRepository.findById(1L)).willReturn(Optional.of(purchase));
        mvc.perform(get("/purchases/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("songID", is(Integer.valueOf(purchase.getSongID().toString()))));
    }

    @Test
    public void findPurchase() throws Exception {

	    List<Purchase> p = new ArrayList<>();
        Purchase purchase = new Purchase();
        purchase.setSongID(1L);
        purchase.setUserID(1L);
        p.add(purchase);

        given(purchasesRepository.getAllBySongID(1L)).willReturn(p);
        mvc.perform(get("/purchases/find")
                .contentType("application/json")
                .param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("songID", is(Integer.valueOf(purchase.getSongID().toString()))));
    }

    @Test
    public void checkPurchase() throws Exception {

        Purchase purchase = new Purchase();
        purchase.setUserID(1L);
        purchase.setSongID(2L);

        given(purchasesRepository.findBySongIDAndUserID(2L, 1L)).willReturn(purchase);
        mvc.perform(get("/purchases/check")
                .contentType("application/json")
                .param("user_id", "1")
                .param("song_id", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("songID", is(Integer.valueOf(purchase.getSongID().toString()))));
    }

    @Test
    public void ratePurchase() throws Exception {

        Purchase purchase = new Purchase();
        purchase.setUserID(1L);
        purchase.setSongID(2L);

        given(purchasesRepository.findById(1L)).willReturn(Optional.of(purchase));
        mvc.perform(post("/purchases/1/rate")
                .contentType("application/json")
                .param("rating", "4"))
                .andExpect(status().isOk());

        mvc.perform(get("/purchases/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("rating", is(4)));
    }
}
