package com.jacketus.RSOI_Lab2.purchasesservice.service;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import com.jacketus.RSOI_Lab2.purchasesservice.exception.PurchaseNotFoundException;
import com.jacketus.RSOI_Lab2.purchasesservice.repository.PurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchasesServiceImplementation implements PurchasesService{
    private final PurchasesRepository purchasesRepository;

    @Autowired
    public PurchasesServiceImplementation(PurchasesRepository purchasesRepository) {
        this.purchasesRepository = purchasesRepository;
    }

    @Override
    public List<Purchase> getAllPurchases() {
        return purchasesRepository.findAll();
    }

    @Override
    public Purchase findPurchaseById(Long id) throws PurchaseNotFoundException{
        return purchasesRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));
    }

    @Override
    public Purchase findPurchaseByUserID(Long userID) throws PurchaseNotFoundException{
        Purchase purchase = purchasesRepository.findByUserID(userID);
        if (purchase == null)
            throw new PurchaseNotFoundException(userID);
        return purchase;
    }

    @Override
    public Purchase findPurchaseBySongID(Long songID) throws PurchaseNotFoundException{
        Purchase purchase = purchasesRepository.findBySongID(songID);
        if (purchase == null)
            throw new PurchaseNotFoundException(songID);
        return purchase;
    }

    @Override
    public boolean checkPurchaseBySongForUser(Long userID, Long songID) {
        Purchase purchase = purchasesRepository.findBySongIDAndUserID(songID, userID);
        if (purchase == null)
            return false;
        return true;
    }

    @Override
    public void createPurchase(Purchase purchase) {
        purchasesRepository.save(purchase);
    }
}
