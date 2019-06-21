package com.jacketus.RSOI_Lab2.gatewayservice.service;

import org.apache.http.HttpResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


public interface GatewayService {

    String oauth_getcode(String auth_url, String client_id, String redirect_uri, String response_type) throws IOException;

    String oauth_exchangecode(String auth_url, String code, String redirect_uri, String client_cred) throws IOException;

    File downloadBook(Long id) throws IOException;

    // Проверка всех всех МС
    String checkAllServices() throws IOException;

    // Просмотреть все книги
    String getBooks(PageRequest p) throws IOException;

    // Просмотреть одну книгу
    String getBookByID(@PathVariable Long bookID) throws IOException;

    // Посмотреть информацию о пользователе
    String getUserById(Long userId) throws IOException;

    // Посмотреть все книги пользователя (все его покупки)
    ResponseEntity getBooksByUser(@PathVariable Long userId) throws IOException;

    // Посмотреть книгу пользователя (рейтинг)
    String getUserBook(@PathVariable Long userId, @PathVariable Long bookId) throws IOException;

    // Посмотреть информацию о покупке
    String getLicense(@PathVariable Long ID) throws IOException;

    // Покупка книги
    ResponseEntity licenseBook(@RequestBody String license) throws IOException;

    // Добавить пользователя
    ResponseEntity addUser(@RequestBody String user) throws IOException;

    // Оценка книги
    ResponseEntity addRatingForBook(@PathVariable Long userID, @PathVariable Long bookID, @RequestParam(value = "rating") int rate) throws IOException;

    // Добавить книгу
    HttpResponse addBook(@RequestBody String book) throws IOException;

    // Запросить токен у URL
    HttpResponse askToken(String url, String clientCred) throws IOException;

    HttpResponse register_user(String auth_url, String user) throws IOException;

    // Проверить токен
    HttpResponse checkToken(String auth_url, String token) throws IOException;

    // Найти пользователя по имени
    String getUserByLogin(String username) throws IOException;

    void uploadBook(@RequestParam(value = "file") MultipartFile book, String id) throws IOException;

    String searchBooks(String body, PageRequest p) throws IOException;

    String putUser(String user) throws Exception;
}