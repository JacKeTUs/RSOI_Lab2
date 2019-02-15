package com.jacketus.RSOI_Lab2.gatewayservice.service;

import org.apache.http.HttpResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


public interface GatewayService {

    String oauth_getcode(String auth_url, String client_id, String redirect_uri, String response_type) throws IOException;

    String oauth_exchangecode(String auth_url, String code, String redirect_uri, String client_cred) throws IOException;

    // Проверка всех всех МС
    String checkAllServices() throws IOException;

    // Просмотреть все песни
    String getSongs(PageRequest p) throws IOException;

    // Просмотреть одну песню
    String getSongByID(@PathVariable Long songID) throws IOException;

    // Посмотреть информацию о пользователе
    String getUserById(Long userId) throws IOException;

    // Посмотреть все песни пользователя (все его покупки)
    String getSongsByUser(@PathVariable Long userId) throws IOException;

    // Посмотреть песню пользователя (рейтинг)
    String getUserSong(@PathVariable Long userId, @PathVariable Long songId) throws IOException;

    // Посмотреть информацию о покупке
    String getPurchase(@PathVariable Long ID) throws IOException;

    // Покупка песни
    void purchaseSong(@RequestBody String purchase) throws IOException;

    // Добавить пользователя
    void addUser(@RequestBody String user) throws IOException;

    // Оценка песни
    void addRatingForSong(@PathVariable Long userID, @PathVariable Long songID, @RequestParam(value = "rating") int rate) throws IOException;

    // Добавить песню
    void addSong(@RequestBody String song) throws IOException;

    // Запросить токен у URL
    HttpResponse askToken(String url, String clientCred) throws IOException;

    // Проверить токен
    HttpResponse checkToken(String auth_url, String token) throws IOException;

    // Найти пользователя по имени
    String getUserByLogin(String username) throws IOException;
}