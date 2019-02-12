package com.jacketus.RSOI_Lab2.gatewayservice.service;

import org.json.JSONException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.io.IOException;


public interface GatewayService {

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

    // Проверить токен
    String checkToken(String token) throws IOException;
}
