package com.jacketus.RSOI_Lab2.gatewayservice.controller;

import com.jacketus.RSOI_Lab2.gatewayservice.service.GatewayService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    // Регистрация
    @PostMapping(path = "/register_user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register_user(@RequestBody String user, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        logger.info("[GET] /register_user");

        // Проверяем данный нам токен у аут.сервиса
       /* token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }
*/
        // http://localhost:8081/oauth/token?grant_type=password&username=user&password=pass
        HttpResponse hr1 = gatewayService.register_user(authServiceUrl, user);

        return ResponseEntity.status(hr1.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr1.getEntity()));
    }


    @GetMapping(path = "/users/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserByLogin(@RequestParam(value = "username") String username, @RequestHeader("Authorization") String token) throws IOException, JSONException {
        logger.info("[GET] /users/find username="+username);

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        return ResponseEntity.ok(gatewayService.getUserByLogin(username));
    }


    @PostMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("book_id") String id,
                                 RedirectAttributes redirectAttributes,
                                 @RequestHeader("Authorization") String token) throws IOException, JSONException {
        logger.info("[POST] /upload, book_id: " + id + ", filename: "  + file.getOriginalFilename());

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        gatewayService.uploadBook(file, id);
        return ResponseEntity.ok("ok");
    }


    // Посмотреть список
    @GetMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBooks(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        logger.info("[GET] /books, page: " + page + ", size: " + size);

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }


        PageRequest p;
        p = PageRequest.of(page, size);
        return ResponseEntity.ok(gatewayService.getBooks(p));
    }

    // Посмотреть одну книгу
    @GetMapping(path = "/books/{bookID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBookByID(@PathVariable Long bookID, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /books/"+bookID);
        return ResponseEntity.ok(gatewayService.getBookByID(bookID));
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
    @GetMapping(path = "/users/{userId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBooksByUser(@PathVariable Long userId, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /users/" + userId + "/books");
        return gatewayService.getBooksByUser(userId);
    }

    // Посмотреть песню пользователя
    @GetMapping(path = "/users/{userId}/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBookByUser(@PathVariable Long userId, @PathVariable Long bookId, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /users/" + userId + "/books/" + bookId);
        return ResponseEntity.ok(gatewayService.getUserBook(userId, bookId));
    }

    // Посмотреть информацию о покупке
    @GetMapping(value = "/licenses/{licenseID}")
    public ResponseEntity getBookLicenses(@PathVariable Long licenseID, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /licenses/" + licenseID);
        gatewayService.getLicense(licenseID);
        return ResponseEntity.ok("ok");
    }

    // Покупка песни
    @PostMapping(value = "/license")
    public ResponseEntity licenseBook(@RequestBody String license, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[POST] /license, license: " + license);

        return gatewayService.licenseBook(license);
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

        logger.info("[POST] /users , user: ", user);

        return gatewayService.addUser(user);
    }

    // Добавить рейтинг песне
    @PostMapping(value = "/users/{userID}/books/{bookID}/rate")
    public ResponseEntity addRatingForBook(@PathVariable Long userID, @PathVariable Long bookID, @RequestParam(value = "rating") int rate, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[POST] /users/" + userID + "/books/" + bookID + "/rate, rating: " + rate);

        return gatewayService.addRatingForBook(userID, bookID, rate);
    }

    // Добавить книгу
    @PostMapping(value = "/books")
    public ResponseEntity<String> addBook(@RequestBody String book, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[POST] /books, book: ", book);

        HttpResponse nb = gatewayService.addBook(book);

        return ResponseEntity.status(nb.getStatusLine().getStatusCode()).body(EntityUtils.toString(nb.getEntity()));
    }


    // Скачать книгу
    @GetMapping(value = "/books/{id}/download")
    public ResponseEntity<InputStreamResource> downloadBook(@PathVariable Long id, @RequestHeader("Authorization") String token) throws IOException {

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            System.out.println("Error");// return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        logger.info("[GET] /books/{id}/download ");

        File encrBook = gatewayService.downloadBook(id);
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(encrBook));
            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encrBook.getName())
                    // Content-Type
                    .contentType(org.springframework.http.MediaType.valueOf("text/plain"))
                    // Contet-Length
                    .contentLength(encrBook.length()) //
                    .body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return (ResponseEntity<InputStreamResource>) ResponseEntity.status(404);
        // return ResponseEntity.status(nb.getStatusLine().getStatusCode()).body(EntityUtils.toString(nb.getEntity()));
    }



    // Поиск книг
    @PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity searchBooks(@RequestBody String search, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestHeader("Authorization") String token) throws IOException, JSONException {

        logger.info("[GET] /search, page: " + page + ", size: " + size);

        // Проверяем данный нам токен у аут.сервиса
        token = token.replace("Bearer ","");
        HttpResponse hr = gatewayService.checkToken(authServiceUrl, token);
        if (hr.getStatusLine().getStatusCode() != 200) {
            return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
        }

        PageRequest p;
        p = PageRequest.of(page, size);
        return ResponseEntity.ok(gatewayService.searchBooks(search, p));
    }

    @GetMapping(value = "/check_health")
    public ResponseEntity checkHealth() throws IOException {
        logger.info("[GET] /check_health ");

        return ResponseEntity.ok(gatewayService.checkAllServices());
    }

    @PutMapping(value="/users/edit")
    public ResponseEntity putUser(@RequestBody String user) throws Exception {
        logger.info("[PUT] /users/edit");
        gatewayService.putUser(user);
        return  ResponseEntity.ok("ok");
    }
}