package com.jacketus.RSOI_Lab2.purchasesservice;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import com.jacketus.RSOI_Lab2.purchasesservice.exception.PurchaseNotFoundException;
import com.jacketus.RSOI_Lab2.purchasesservice.repository.PurchasesRepository;
import com.jacketus.RSOI_Lab2.purchasesservice.service.PurchasesService;
import com.jacketus.RSOI_Lab2.purchasesservice.service.PurchasesServiceImplementation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class PurchasesServiceTest {
    private PurchasesService purchasesService;

    @Mock
    PurchasesRepository purchasesRepository;

    @Before
    public void setUp(){
        initMocks(this);
        purchasesService = new PurchasesServiceImplementation(purchasesRepository);
    }

    @Test
    public void shouldReturnAllPurchases(){
        List<Purchase> purchases= new ArrayList<>();
        Purchase purchase= new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        given(purchasesRepository.findAll()).willReturn(purchases);
        List<Purchase> purchasesReturned = purchasesService.getAllPurchases();
        assertThat(purchasesReturned, is(purchases));
    }

    @Test
    public void shouldCreatePurchase(){
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        assertThat(purchasesRepository.save(purchase), is(purchase));
        Purchase saved = purchasesRepository.save(purchase);

        Long s = saved.getSongID();
        assertEquals(Optional.of(5L), Optional.ofNullable(s));
        assertEquals(Optional.of(1L), Optional.ofNullable(saved.getUserID()));
    }

    @Test
    public void shouldFindPurchaseBySongID() throws PurchaseNotFoundException {
        List<Purchase> purchases= new ArrayList<>();
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);
        purchases.add(purchase);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        given(purchasesRepository.getAllBySongID(5L)).willReturn(purchases);

        purchasesService.createPurchase(purchase);
        assertThat(purchasesService.findPurchaseBySongID(5L).get(0), is(purchase));
    }

    @Test
    public void shouldFindPurchaseByUserID() throws PurchaseNotFoundException {
        List<Purchase> purchases= new ArrayList<>();
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);
        purchases.add(purchase);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        given(purchasesRepository.getAllByUserID(1L)).willReturn(purchases);

        purchasesService.createPurchase(purchase);
        assertThat(purchasesService.findPurchaseByUserID(1L).get(0), is(purchase));
    }

    @Test
    public void shouldFindPurchaseByUserIDAndSongID() throws PurchaseNotFoundException {
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        given(purchasesRepository.findBySongIDAndUserID(5L,1L)).willReturn(purchase);

        purchasesService.createPurchase(purchase);

        if (purchasesService.checkPurchaseBySongForUser(1L, 5L) == null)
            fail();
        if (purchasesService.checkPurchaseBySongForUser(1L, 6L) != null)
            fail();

    }

    @Test
    public void shouldRatePurchase() throws PurchaseNotFoundException {
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        given(purchasesRepository.findById(purchase.getId())).willReturn(Optional.of(purchase));

        Purchase saved = purchasesService.createPurchase(purchase);
        purchasesService.rate(saved.getId(), 4);
        int rate = purchasesService.findPurchaseById(saved.getId()).getRating();

        assertEquals(4, rate);
    }

    @Test
    public void shouldHash(){
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        try {
            given(purchasesRepository.save(purchase)).willReturn(purchase);
            given(purchasesRepository.findById(purchase.getId())).willReturn(Optional.of(purchase));
            Purchase saved = purchasesService.createPurchase(purchase);

            assertNotNull(saved.hashCode());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldToString(){
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);
        String s = "Purchase{id=null, userID='1', songID='5', rating='0'}";

        try {
            given(purchasesRepository.save(purchase)).willReturn(purchase);
            given(purchasesRepository.findById(purchase.getId())).willReturn(Optional.of(purchase));
            Purchase saved = purchasesService.createPurchase(purchase);

            System.out.println(saved.toString());
            assertEquals(s, saved.toString());
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldEquals(){
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        try {
            given(purchasesRepository.save(purchase)).willReturn(purchase);
            given(purchasesRepository.findById(purchase.getId())).willReturn(Optional.of(purchase));
            Purchase saved = purchasesService.createPurchase(purchase);

            boolean eq = saved.equals(purchase);
            assertEquals(true, eq);
        }
        catch (Exception ex){
            fail();
        }
    }

}
