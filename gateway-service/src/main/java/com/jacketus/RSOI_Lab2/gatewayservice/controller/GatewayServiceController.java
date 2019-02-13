package com.jacketus.RSOI_Lab2.gatewayservice.controller;

import com.jacketus.RSOI_Lab2.gatewayservice.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class GatewayServiceController {
    private final GatewayService gatewayService;
    private Logger logger;
    final private String authServiceUrl = "http://localhost:8081";

    @Autowired
    public GatewayServiceController(GatewayService gatewayService){
        logger = LoggerFactory.getLogger(GatewayServiceController.class);
        this.gatewayService = gatewayService;
    }

    // Авторизация OAUTH
    @GetMapping(path = "/oauth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity oauthlogin(
            //@RequestParam(value = "grant_type") String grant_type,
            @RequestParam(value = "client_id") String client_id,
            @RequestParam(value = "redirect_uri") String redirect_uri
            /*@RequestParam(value = "response_type") String response_type*/) throws IOException, JSONException {

        logger.info("[GET] /oauth/login");

        // Редирект к аут.сервису с нашими параметрами...
        String r = gatewayService.oauth_getcode(authServiceUrl, client_id, redirect_uri, "code");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", r);
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    // http://localhost:8081/oauth/token?grant_type=authorization_code&code=MlBHlY&redirect_uri=http%3A%2F%2Fexample.com
    // Обмен кода OAUTH
    @GetMapping(path = "/oauth/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity oauth_token(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "redirect_uri") String redirect_uri,
            @RequestHeader("Authorization") String client_cred) throws IOException, JSONException {

        logger.info("[GET] /oauth/token");

        // Меняем код у аут.сервиса

        String clientCode = "";
        client_cred = client_cred.replace("Basic","");

        String r = gatewayService.oauth_exchangecode(authServiceUrl, code, redirect_uri, client_cred);

        return ResponseEntity.ok(r);
    }

    // Авторизация
    @GetMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestHeader("Authorization") String clientCred) throws IOException, JSONException {

        logger.info("[GET] /login");
        // Берём токен для клиентского приложения у аут.сервиса
        //token = token.replace("Bearer ","");
        String clientToken = "";
        clientCred = clientCred.replace("Basic", "");
        // http://localhost:8081/oauth/token?grant_type=password&username=user&password=pass
        HttpResponse hr = gatewayService.askToken(authServiceUrl + "/oauth/token?grant_type=password&username="+username+"&password="+password, clientCred);

        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    // Посмотреть список песен
    @GetMapping(path = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSongs(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        logger.info("[GET] /songs, page: " + page + ", size: " + size);

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }


        PageRequest p;
        p = PageRequest.of(page, size);
        return ResponseEntity.ok(gatewayService.getSongs(p));
    }

    // Посмотреть одну песню
    @GetMapping(path = "/songs/{songID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSongByID(@PathVariable Long songID, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /songs/"+songID);
        return ResponseEntity.ok(gatewayService.getSongByID(songID));
    }

    // Посмотреть информацию о пользователе
    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /users/" +userId);
        return ResponseEntity.ok(gatewayService.getUserById(userId));
    }

    // Посмотреть все песни пользователя (все его покупки)
    @GetMapping(path = "/users/{userId}/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSongsByUser(@PathVariable Long userId, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /users/" + userId + "/songs");
        return ResponseEntity.ok(gatewayService.getSongsByUser(userId));
    }

    // Посмотреть песню пользователя
    @GetMapping(path = "/users/{userId}/songs/{songId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSongsByUser(@PathVariable Long userId, @PathVariable Long songId, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /users/" + userId + "/songs/" + songId);
        return ResponseEntity.ok(gatewayService.getUserSong(userId, songId));
    }

    // Посмотреть информацию о покупке
    @GetMapping(value = "/purchases/{purchaseID}")
    public ResponseEntity getSongPurchases(@PathVariable Long purchaseID, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /purchases/" + purchaseID);
        gatewayService.getPurchase(purchaseID);
        return ResponseEntity.ok("ok");
    }

    // Покупка песни
    @PostMapping(value = "/purchase")
    public ResponseEntity purchaseSong(@RequestBody String purchase, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[POST] /purchase, purchase: " + purchase);
        gatewayService.purchaseSong(purchase);
        return ResponseEntity.ok("ok");
    }

    // Добавить пользователя
    @PostMapping(value = "/users")
    public ResponseEntity addUser(@RequestBody String user, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[POST] /users ", "user: ", user);
        gatewayService.addUser(user);
        return ResponseEntity.ok("ok");
    }

    // Добавить рейтинг песне
    @PostMapping(value = "/users/{userID}/songs/{songID}/rate")
    public ResponseEntity addRatingForSong(@PathVariable Long userID, @PathVariable Long songID, @RequestParam(value = "rating") int rate, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[POST] /users/" + userID + "/songs/" + songID + "/rate, rating: " + rate);
        gatewayService.addRatingForSong(userID, songID, rate);
        return ResponseEntity.ok("ok");
    }

    // Добавить песню
    @PostMapping(value = "/songs")
    public ResponseEntity addSong(@RequestBody String song, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[POST] /songs ", "song: ", song);
        gatewayService.addSong(song);
        return ResponseEntity.ok("ok");
    }
}