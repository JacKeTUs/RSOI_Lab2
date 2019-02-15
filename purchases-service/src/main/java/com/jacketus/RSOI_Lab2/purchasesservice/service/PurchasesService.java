package com.jacketus.RSOI_Lab2.purchasesservice.service;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import com.jacketus.RSOI_Lab2.purchasesservice.exception.PurchaseNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PurchasesService {
    Purchase findPurchaseById(Long id) throws PurchaseNotFoundException;
    List<Purchase> findPurchaseByUserID(Long userID) throws PurchaseNotFoundException;
    List<Purchase> findPurchaseBySongID(Long songID) throws PurchaseNotFoundException;
    Purchase checkPurchaseBySongForUser(Long userID, Long songID) throws PurchaseNotFoundException;
    List<Purchase> getAllPurchases();
    Purchase createPurchase(Purchase purchase);
    void rate(Long id, int rating) throws PurchaseNotFoundException;

    ResponseEntity check_health();
}

