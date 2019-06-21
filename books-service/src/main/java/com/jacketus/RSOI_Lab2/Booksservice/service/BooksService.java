package com.jacketus.RSOI_Lab2.Booksservice.service;

import com.jacketus.RSOI_Lab2.Booksservice.entity.Book;
import com.jacketus.RSOI_Lab2.Booksservice.exception.BookNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface BooksService {
    Page<Book> getAllBooks(PageRequest p);
    Page<Book> searchBooks(String searchJSON, PageRequest p);
    Book createBook(Book book);
    Book getBookByID(Long id) throws BookNotFoundException;
  //  void setRating(Long id, double rating) throws BookNotFoundException;
  //  double getRating(Long id) throws BookNotFoundException;
  //  int getRateNum(Long id) throws BookNotFoundException;
    void incBuyNum(Long id) throws BookNotFoundException;
    int getBuyNum(Long id) throws BookNotFoundException;

    ResponseEntity upload(MultipartFile file);

    Book putBook(Book p) throws BookNotFoundException;

    ResponseEntity check_health();

    File getBookFile(Long id) throws BookNotFoundException;
}