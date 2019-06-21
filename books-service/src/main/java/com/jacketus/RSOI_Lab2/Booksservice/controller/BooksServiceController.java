package com.jacketus.RSOI_Lab2.Booksservice.controller;

import com.jacketus.RSOI_Lab2.Booksservice.*;
import com.jacketus.RSOI_Lab2.Booksservice.entity.Book;
import com.jacketus.RSOI_Lab2.Booksservice.exception.BookNotFoundException;
import com.jacketus.RSOI_Lab2.Booksservice.service.BookEncryptor;
import com.jacketus.RSOI_Lab2.Booksservice.service.BooksService;
import com.jacketus.RSOI_Lab2.Booksservice.service.BooksServiceImplementation;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.MediaType;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@Slf4j
public class BooksServiceController {
    private final BooksService booksService;
    Logger logger;

    @Autowired
    public BooksServiceController(BooksService _booksService){
        this.booksService = _booksService;
        logger  = LoggerFactory.getLogger(BooksServiceImplementation.class);
    }

    // Получение списка
    @GetMapping(value = "/books")
    public Page<Book> getAllBooks(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size){
        logger.info("[GET] /books page=" + page + " size=" + size);
        PageRequest p = PageRequest.of(page, size);
        return booksService.getAllBooks(p);
    }

    // Поиск книг
    @PostMapping(value = "/search")
    public Page<Book> searchByAuthor(@RequestBody String search, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {

        logger.info("[GET] /search page=" + page + " size=" + size);
        PageRequest p = PageRequest.of(page, size);

        return booksService.searchBooks(search, p);
    }


    // Получение информации об одной
    @GetMapping(value = "/books/{id}")
    public Book getBookByID(@PathVariable Long id) throws BookNotFoundException {
        logger.info("[GET] /books/ " + id);
        return booksService.getBookByID(id);
    }

    // Добавление на сайт
    @PostMapping(value = "/books")
    public Book createBook(@RequestBody Book book){
        logger.info("[POST] /books " + book.toString());
        return booksService.createBook(book);
    }
/*
    // Оценка
    @PostMapping(value = "/books/{id}/rate")
    public void setRating(@PathVariable Long id, @RequestParam(value = "rating") int rate) throws BookNotFoundException {
        logger.info("[POST] /books/" + id + "/rate, rating:" + rate);
        booksService.setRating(id, rate);
    }
*/
    // Покупка
    @PostMapping(value = "/books/{id}/buy")
    public void buyBook(@PathVariable Long id) throws BookNotFoundException {
        logger.info("[POST] /books/" + id + "/buy");
        booksService.incBuyNum(id);
    }

    @PutMapping(value="/books/edit")
    public void putUser(@RequestBody Book p) throws BookNotFoundException {
        logger.info("[PUT] /books/edit");
        booksService.putBook(p);
    }

    @GetMapping(value = "/check_health")
    public ResponseEntity checkHealth(){
        logger.info("[GET] /check_health");

        return booksService.check_health();
    }

    @PostMapping(value = "/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file){
        logger.info("[POST] /upload, filename: " + file.getOriginalFilename());

        return booksService.upload(file);
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity downloadBook(@PathVariable Long id) throws BookNotFoundException {
        logger.info("[GET] /download/" + id + "");
        File origBook = booksService.getBookFile(id);
        File encrBook = new File(origBook.toPath().toString() + "e");
        try {
            encrBook.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BookEncryptor.encryptBook("offline", origBook, encrBook, null);
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
        return (ResponseEntity) ResponseEntity.status(502);
    }

}
