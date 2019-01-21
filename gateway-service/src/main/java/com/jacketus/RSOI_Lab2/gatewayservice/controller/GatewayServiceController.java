package com.jacketus.RSOI_Lab2.gatewayservice.controller;

import com.jacketus.RSOI_Lab2.gatewayservice.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api")
public class GatewayServiceController {
    private final GatewayService gatewayService;
    private Logger logger;

    @Autowired
    public GatewayServiceController(GatewayService gatewayService){
        logger = LoggerFactory.getLogger(GatewayServiceController.class);
        this.gatewayService = gatewayService;
    }

    // Посмотреть список песен
    @GetMapping(path = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSongs(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) throws IOException, JSONException {
        logger.info("[GET] /songs, page: " + page + ", size: " + size);
        PageRequest p;
        p = PageRequest.of(page, size);


        return gatewayService.getSongs(p);
    }

    // Посмотреть одну песню
    @GetMapping(path = "/songs/{songID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSongByID(@PathVariable Long songID) throws IOException, JSONException {
        logger.info("[GET] /songs/"+songID);
        return gatewayService.getSongByID(songID);
    }

    // Посмотреть информацию о пользователе
    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserById(@PathVariable Long userId) throws IOException {
        logger.info("[GET] /users/" +userId);
        return gatewayService.getUserById(userId);
    }

    // Посмотреть все песни пользователя (все его покупки)
    @GetMapping(path = "/users/{userId}/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSongsByUser(@PathVariable Long userId) throws IOException, JSONException {
        logger.info("[GET] /users/" + userId + "/songs");
        return gatewayService.getSongsByUser(userId);
    }

    // Посмотреть песню пользователя
    @GetMapping(path = "/users/{userId}/songs/{songId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSongsByUser(@PathVariable Long userId, @PathVariable Long songId) throws IOException, JSONException {
        logger.info("[GET] /users/" + userId + "/songs/" + songId);
        return gatewayService.getUserSong(userId, songId);
    }

    // Посмотреть информацию о покупке
    @GetMapping(value = "/purchases/{purchaseID}")
    public void getSongPurchases(@PathVariable Long purchaseID) throws IOException {
        logger.info("[GET] /purchases/" + purchaseID);
        gatewayService.getPurchase(purchaseID);
    }

    // Покупка песни
    @PostMapping(value = "/purchase")
    public void purchaseSong(@RequestBody String purchase) throws IOException {
        logger.info("[POST] /purchase, purchase: " + purchase);
        gatewayService.purchaseSong(purchase);
    }

    // Добавить пользователя
    @PostMapping(value = "/users")
    public void addUser(@RequestBody String user) throws IOException {
        logger.info("[POST] /users ", "user: ", user);
        gatewayService.addUser(user);
    }

    // Добавить рейтинг песне
    @PostMapping(value = "/users/{userID}/songs/{songID}/rate")
    public void addRatingForSong(@PathVariable Long userID, @PathVariable Long songID, @RequestParam(value = "rating") int rate) throws IOException {
        logger.info("[POST] /users/" + userID + "/songs/" + songID + "/rate, rating: " + rate);
        gatewayService.addRatingForSong(userID, songID, rate);
    }

    // Добавить песню
    @PostMapping(value = "/songs")
    public void addSong(@RequestBody String song) throws IOException {
        logger.info("[POST] /songs ", "song: ", song);
        gatewayService.addSong(song);
    }







}
