package com.jacketus.RSOI_Lab2.purchasesservice.service;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import com.jacketus.RSOI_Lab2.purchasesservice.exception.PurchaseNotFoundException;

import java.util.List;

public interface PurchasesService {
    Purchase findPurchaseById(Long id) throws PurchaseNotFoundException;
    Purchase findPurchaseByUserID(Long userID) throws PurchaseNotFoundException;
    Purchase findPurchaseBySongID(Long songID) throws PurchaseNotFoundException;
    boolean checkPurchaseBySongForUser(Long userID, Long songID);
    List<Purchase> getAllPurchases();
    void createPurchase(Purchase purchase);
    //void addPurchase(Long songID, Long userID);
}

