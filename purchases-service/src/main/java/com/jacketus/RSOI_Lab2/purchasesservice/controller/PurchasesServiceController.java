package com.jacketus.RSOI_Lab2.purchasesservice.controller;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import com.jacketus.RSOI_Lab2.purchasesservice.exception.PurchaseNotFoundException;
import com.jacketus.RSOI_Lab2.purchasesservice.service.PurchasesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PurchasesServiceController {
    private PurchasesService purchasesService;
    private Logger logger;

    @Autowired
    PurchasesServiceController(PurchasesService purchasesService){
        this.purchasesService = purchasesService;
        logger = LoggerFactory.getLogger(PurchasesServiceController.class);
    }


    @PostMapping(value = "/purchases")
    public void addPurchase(@RequestBody Purchase purchase) {
        logger.info("[POST] /purchases");
        purchasesService.createPurchase(purchase);
    }

    @GetMapping(value = "/purchases")
    public List<Purchase> getAllPurchases(){
        logger.info("[GET] /purchases");
        return purchasesService.getAllPurchases();
    }

    @GetMapping(value = "/purchases/{id}")
    public Purchase getPurchaseById(@PathVariable Long id) throws PurchaseNotFoundException {
        logger.info("[GET] /purchases/" + id);
        return purchasesService.findPurchaseById(id);
    }

    @GetMapping(value = "/purchases/find")
    public Purchase getPurchaseByUserID(@RequestParam(value = "user_id") Long userID) throws PurchaseNotFoundException {
        logger.info("[GET] /purchases/find ", userID);
        return purchasesService.findPurchaseByUserID(userID);
    }

    @GetMapping(value = "/purchases/check")
    public boolean getPurchaseByUserID(@RequestParam(value = "user_id") Long userID, @RequestParam(value = "song_id") Long songID ) throws PurchaseNotFoundException {
        logger.info("[GET] /purchases/find ", userID);
        return purchasesService.checkPurchaseBySongForUser(userID, songID);
    }

}
