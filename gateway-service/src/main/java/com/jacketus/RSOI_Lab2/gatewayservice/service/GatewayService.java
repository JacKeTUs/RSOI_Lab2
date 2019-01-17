package com.jacketus.RSOI_Lab2.gatewayservice.service;

import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


public interface GatewayService {

    // Посмотреть информацию о пользователе
    String getUserById(Long userId) throws IOException;

    // Посмотреть все песни пользователя (все его покупки)
    String getSongsByUser(@PathVariable Long userId) throws IOException;

    // Просмотреть все песни
    String getSongs() throws IOException;

    // Просмотреть одну песню
    String getSongByID(@PathVariable Long songID) throws IOException;;

    // Добавить песню
    void addSong(@RequestBody String song) throws IOException;

    // Добавить рейтинга песне
    void addRatingForSong(@PathVariable Long songID, @RequestParam(value = "rating") double rate) throws IOException;

    // Посмотреть все покупки песни
    void getSongPurchases(@PathVariable Long songID) throws IOException;

    // Добавить песню пользователю
    void purchaseSong(@RequestBody String purchase) throws IOException;
}
