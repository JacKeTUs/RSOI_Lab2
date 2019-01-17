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
    }

    @Test
    public void shouldFindPurchaseBySongID() throws PurchaseNotFoundException {
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        given(purchasesRepository.findBySongID(5L)).willReturn(purchase);

        purchasesService.createPurchase(purchase);
        assertThat(purchasesService.findPurchaseBySongID(5L), is(purchase));
    }

    @Test
    public void shouldFindPurchaseByUserID() throws PurchaseNotFoundException {
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        given(purchasesRepository.findByUserID(1L)).willReturn(purchase);

        purchasesService.createPurchase(purchase);
        assertThat(purchasesService.findPurchaseByUserID(1L), is(purchase));
    }

    @Test
    public void shouldFindPurchaseByUserIDAndSongID() throws PurchaseNotFoundException {
        Purchase purchase = new Purchase();
        purchase.setSongID(5L);
        purchase.setUserID(1L);

        given(purchasesRepository.save(purchase)).willReturn(purchase);
        given(purchasesRepository.findBySongIDAndUserID(5L,1L)).willReturn(purchase);

        purchasesService.createPurchase(purchase);
        assertEquals(purchasesService.checkPurchaseBySongForUser(1L, 5L), true);
        assertEquals(purchasesService.checkPurchaseBySongForUser(1L, 6L), false);
    }

}
