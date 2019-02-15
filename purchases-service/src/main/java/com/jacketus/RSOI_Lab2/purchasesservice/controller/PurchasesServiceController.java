package com.jacketus.RSOI_Lab2.purchasesservice.controller;

import com.jacketus.RSOI_Lab2.purchasesservice.entity.Purchase;
import com.jacketus.RSOI_Lab2.purchasesservice.exception.PurchaseNotFoundException;
import com.jacketus.RSOI_Lab2.purchasesservice.service.PurchasesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PurchasesServiceController {
    private PurchasesService purchasesService;
    private Logger logger;

    @Autowired
    public PurchasesServiceController(PurchasesService purchasesService){
        this.purchasesService = purchasesService;
        logger = LoggerFactory.getLogger(PurchasesServiceController.class);
    }

    // Покупка.
    @PostMapping(value = "/purchases")
    public void addPurchase(@RequestBody Purchase purchase) {
        logger.info("[POST] /purchases");
        purchasesService.createPurchase(purchase);
    }

    // Получение информации об оплате
    @GetMapping(value = "/purchases/{id}")
    public Purchase getPurchaseById(@PathVariable Long id) throws PurchaseNotFoundException {
        logger.info("[GET] /purchases/" + id);
        return purchasesService.findPurchaseById(id);
    }

    // Все купленные песни пользователем
    @GetMapping(value = "/purchases/find")
    public List<Purchase> getPurchaseByUserID(@RequestParam(value = "user_id") Long userID) throws PurchaseNotFoundException {
        logger.info("[GET] /purchases/find ", userID);
        return purchasesService.findPurchaseByUserID(userID);
    }

    // Поиск лицензии на песню на пользователя.
    @GetMapping(value = "/purchases/check")
    public Purchase checkPurchaseByUserID(@RequestParam(value = "user_id") Long userID, @RequestParam(value = "song_id") Long songID ) throws PurchaseNotFoundException {
        logger.info("[GET] /purchases/check ", userID);
        Purchase p = null;
        try {
            p = purchasesService.checkPurchaseBySongForUser(userID, songID);
        } catch (PurchaseNotFoundException e) {
            e.printStackTrace();
        }
        return p;
    }

    // Оценка.
    @PostMapping(value = "/purchases/{id}/rate")
    public void addPurchase(@PathVariable Long id, @RequestParam(value = "rating") int rating) throws PurchaseNotFoundException {
        logger.info("[POST] /purchases/" + id + "/rate, rating: " + rating);
        purchasesService.rate(id, rating);
    }


    @PutMapping(value="/purchases/edit")
    public void putUser(@RequestBody Purchase p) throws PurchaseNotFoundException {
        logger.info("[PUT] /purchases/edit");
        purchasesService.putPurchase(p);
    }

    @GetMapping(value = "/check_health")
    public ResponseEntity checkHealth(){
        logger.info("[GET] /check_health");

        return purchasesService.check_health();
    }

}
