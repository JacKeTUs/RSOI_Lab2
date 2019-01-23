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
    public Purchase findPurchaseById(Long id) throws PurchaseNotFoundException {
        return purchasesRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));
    }

    @Override
    public List<Purchase> findPurchaseByUserID(Long userID) throws PurchaseNotFoundException{
        List<Purchase> purchase = purchasesRepository.getAllByUserID(userID);
        if (purchase == null)
            throw new PurchaseNotFoundException(userID);
        return purchase;
    }

    @Override
    public List<Purchase> findPurchaseBySongID(Long songID) throws PurchaseNotFoundException {
        List<Purchase> purchase = purchasesRepository.getAllBySongID(songID);
        if (purchase == null)
            throw new PurchaseNotFoundException(songID);
        return purchase;
    }

    @Override
    public Purchase checkPurchaseBySongForUser(Long userID, Long songID) throws PurchaseNotFoundException {
        Purchase purchase = purchasesRepository.findBySongIDAndUserID(songID, userID);
        return purchase;
    }

    @Override
    public Purchase createPurchase(Purchase purchase) {
        System.out.println("CREATE: " + purchase.toString());
        return purchasesRepository.save(purchase);
    }

    @Override
    public void rate(Long id, int rating) throws PurchaseNotFoundException {
        Purchase purchase = purchasesRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));
        purchase.setRating(rating);
        purchasesRepository.save(purchase);
    }
}
