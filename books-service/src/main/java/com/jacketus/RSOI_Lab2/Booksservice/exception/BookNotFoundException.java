package com.jacketus.RSOI_Lab2.Booksservice.exception;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(Long id){
        super("Book with id= " + id + " was not found!");
    }
}
