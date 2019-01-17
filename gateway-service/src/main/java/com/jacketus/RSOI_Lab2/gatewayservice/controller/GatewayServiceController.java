package com.jacketus.RSOI_Lab2.gatewayservice.controller;

import com.jacketus.RSOI_Lab2.gatewayservice.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

    // Посмотреть список песен
    @GetMapping(path = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSongs() throws IOException, JSONException {
        logger.info("[GET] /songs");
        return gatewayService.getSongs();
    }

    // Посмотреть одну песню
    @GetMapping(path = "/songs/{songID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSongByID(@PathVariable Long songID) throws IOException, JSONException {
        logger.info("[GET] /songs/"+songID);
        return gatewayService.getSongByID(songID);
    }

    // Добавить песню
    @PostMapping(value = "/songs/add")
    public void addSong(@RequestBody String song) throws IOException {
        logger.info("[POST] /songs/add ", "song: ", song);
        gatewayService.addSong(song);
    }

    // Добавить рейтинг песне
    @PostMapping(value = "/songs/{songID}/rate")
    public void addRatingForSong(@PathVariable Long songID, @RequestParam(value = "rating") double rate) throws IOException {
        logger.info("[POST] /songs/{songID}/rate ", "songID: ", songID, " rating: ", rate);
        gatewayService.addRatingForSong(songID, rate);
    }

    // Посмотреть все покупки песни
    @GetMapping(value = "/purchases/songs/{songID}")
    public void getSongPurchases(@PathVariable Long songID) throws IOException {
        gatewayService.getSongPurchases(songID);
        logger.info("[GET] /purchases/songs/ ", + songID);
    }

    // Добавить песню пользователю
    @PostMapping(value = "/purchase")
    public void createReview(@RequestBody String purchase) throws IOException {
        logger.info("[POST] /purchase ");
        gatewayService.purchaseSong(purchase);
    }

}
