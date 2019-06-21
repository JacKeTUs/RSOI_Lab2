package com.jacketus.RSOI_Lab2.Booksservice.service;

import com.jacketus.RSOI_Lab2.Booksservice.repository.BooksRepository;
import com.jacketus.RSOI_Lab2.Booksservice.entity.Book;
import com.jacketus.RSOI_Lab2.Booksservice.exception.BookNotFoundException;
import com.jacketus.RSOI_Lab2.Booksservice.storage.StorageService;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BooksServiceImplementation implements BooksService {
    private final BooksRepository booksRepository;
    private final StorageService storageService;

    @Autowired
    public BooksServiceImplementation(BooksRepository booksRepository, StorageService storageService){
        this.booksRepository = booksRepository;
        this.storageService = storageService;
        this.storageService.init();
    }

    @Override
    public Page<Book> getAllBooks(PageRequest p) {
        return booksRepository.findAll(p);
    }

    @Override
    public Page<Book> searchBooks(String searchJSON, PageRequest pageable) {

        JSONParser jp = new JSONParser();
        try {
            JSONObject search = (JSONObject) jp.parse(searchJSON);
            String author = (String) search.get("author");
            String name = (String) search.get("name");
            String description = (String) search.get("description");
            String genre = (String) search.get("genre");

            List<Book> authors = new ArrayList<>(), names= new ArrayList<>(), descrs= new ArrayList<>(), genres= new ArrayList<>();

            if (author != null && !author.isEmpty()) {
                authors = this.booksRepository.findAllByAuthorContaining(author);
                System.out.println(authors.size());
            }
            if (name!= null && !name.isEmpty()) {
                names = this.booksRepository.findAllByNameContaining(name);
                System.out.println(names.size());
            }
            if (description!= null && !description.isEmpty()) {
                descrs = this.booksRepository.findAllByDescriptionContaining(description);
                System.out.println(descrs.size());
            }
            if (genre!= null && !genre.isEmpty()) {
                genres = this.booksRepository.findAllByGenreContaining(genre);
                System.out.println(genres.size());
            }
            List<Book> res = new ArrayList<Book>();
            res.addAll(authors);
            List<Book> n_copy = new ArrayList<>(names);
            n_copy.removeAll(res);
            res.addAll(n_copy);
            n_copy = new ArrayList<>(descrs);
            n_copy.removeAll(res);
            res.addAll(n_copy);
            n_copy = new ArrayList<>(genres);
            n_copy.removeAll(res);
            res.addAll(n_copy);


            int start = (int) pageable.getOffset();
            int end = (start + pageable.getPageSize()) > res.size() ? res.size() : (start + pageable.getPageSize());
            Page<Book> pages = new PageImpl<Book>(res.subList(start, end), pageable, res.size());
            return pages;
        } catch (ParseException e) {
            e.printStackTrace();
        }




        return null;
    }

    @Override
    public Book getBookByID (Long id) throws BookNotFoundException {
        Book book =  booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return book;
    }

    @Override
    public Book createBook(Book book) {
        return booksRepository.saveAndFlush(book);
    }
/*
    @Override
    public int getRateNum(Long id) throws BookNotFoundException{
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return book.getRateNum();
    }

    @Override
    public void setRating(Long id, double rating) throws BookNotFoundException {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.setRating(rating);
        booksRepository.saveAndFlush(book);
    }

    @Override
    public double getRating(Long id) throws BookNotFoundException {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return book.getRating();
    }
*/
    @Override
    public void incBuyNum(Long id) throws BookNotFoundException {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.setBuy_nums(book.getBuy_nums() + 1);
        booksRepository.saveAndFlush(book);
    }

    @Override
    public int getBuyNum(Long id) throws BookNotFoundException {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return book.getBuy_nums();
    }

    @Override
    public ResponseEntity check_health() {
        JSONObject json1 = new JSONObject();
        try {
            json1.put("info", "Book MService");
            json1.put("version", 0.5);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(json1.toString());
    }

    @Override
    public ResponseEntity upload(MultipartFile file) {
        storageService.store(file);

        return ResponseEntity.ok("File uploaded!");
    }


    @Override
    public Book putBook(Book p) throws BookNotFoundException {
        return booksRepository.findById(p.getId()).map(Book -> {
            Book.setBuy_nums(p.getBuy_nums());
           // Book.setRating(p.getRating());
           // Book.setLink(p.getLink());
            Book.setAuthor(p.getAuthor());
            Book.setName(p.getName());
            Book.setDescription(p.getDescription());
            return booksRepository.saveAndFlush(Book);
        }).orElseThrow(() -> new BookNotFoundException(p.getId()));
    }

    @Override
    public File getBookFile(Long id) throws BookNotFoundException {
        return booksRepository.findById(id).map(Book -> {
            Path p = storageService.load(Book.getId() + ".fb2");
            return p.toFile();
        }).orElseThrow(() -> new BookNotFoundException(id));
    }

}
