package com.jacketus.RSOI_Lab2.gatewayservice.controller;

import com.jacketus.RSOI_Lab2.gatewayservice.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getSongs(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[GET] /songs, page: " + page + ", size: " + size);
        PageRequest p;
        p = PageRequest.of(page, size);
        return ResponseEntity.ok(gatewayService.getSongs(p));
    }

    // Посмотреть одну песню
    @GetMapping(path = "/songs/{songID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSongByID(@PathVariable Long songID, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[GET] /songs/"+songID);
        return ResponseEntity.ok(gatewayService.getSongByID(songID));
    }

    // Посмотреть информацию о пользователе
    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String token) throws IOException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[GET] /users/" +userId);
        return ResponseEntity.ok(gatewayService.getUserById(userId));
    }

    // Посмотреть все песни пользователя (все его покупки)
    @GetMapping(path = "/users/{userId}/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSongsByUser(@PathVariable Long userId, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[GET] /users/" + userId + "/songs");
        return ResponseEntity.ok(gatewayService.getSongsByUser(userId));
    }

    // Посмотреть песню пользователя
    @GetMapping(path = "/users/{userId}/songs/{songId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSongsByUser(@PathVariable Long userId, @PathVariable Long songId, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[GET] /users/" + userId + "/songs/" + songId);
        return ResponseEntity.ok(gatewayService.getUserSong(userId, songId));
    }

    // Посмотреть информацию о покупке
    @GetMapping(value = "/purchases/{purchaseID}")
    public ResponseEntity getSongPurchases(@PathVariable Long purchaseID, @RequestHeader("Authorization") String token) throws IOException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[GET] /purchases/" + purchaseID);
        gatewayService.getPurchase(purchaseID);
        return ResponseEntity.ok("ok");
    }

    // Покупка песни
    @PostMapping(value = "/purchase")
    public ResponseEntity purchaseSong(@RequestBody String purchase, @RequestHeader("Authorization") String token) throws IOException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[POST] /purchase, purchase: " + purchase);
        gatewayService.purchaseSong(purchase);
        return ResponseEntity.ok("ok");
    }

    // Добавить пользователя
    @PostMapping(value = "/users")
    public ResponseEntity addUser(@RequestBody String user, @RequestHeader("Authorization") String token) throws IOException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[POST] /users ", "user: ", user);
        gatewayService.addUser(user);
        return ResponseEntity.ok("ok");
    }

    // Добавить рейтинг песне
    @PostMapping(value = "/users/{userID}/songs/{songID}/rate")
    public ResponseEntity addRatingForSong(@PathVariable Long userID, @PathVariable Long songID, @RequestParam(value = "rating") int rate, @RequestHeader("Authorization") String token) throws IOException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[POST] /users/" + userID + "/songs/" + songID + "/rate, rating: " + rate);
        gatewayService.addRatingForSong(userID, songID, rate);
        return ResponseEntity.ok("ok");
    }

    // Добавить песню
    @PostMapping(value = "/songs")
    public ResponseEntity addSong(@RequestBody String song, @RequestHeader("Authorization") String token) throws IOException {

        token = token.replace("Bearer ","");
        if (gatewayService.checkToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        logger.info("[POST] /songs ", "song: ", song);
        gatewayService.addSong(song);
        return ResponseEntity.ok("ok");
    }
}
