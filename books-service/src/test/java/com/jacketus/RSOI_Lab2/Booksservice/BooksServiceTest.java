package com.jacketus.RSOI_Lab2.booksservice;

import com.jacketus.RSOI_Lab2.Booksservice.entity.Book;
import com.jacketus.RSOI_Lab2.Booksservice.exception.BookNotFoundException;
import com.jacketus.RSOI_Lab2.Booksservice.repository.BooksRepository;
import com.jacketus.RSOI_Lab2.Booksservice.service.BooksService;
import com.jacketus.RSOI_Lab2.Booksservice.service.BooksServiceImplementation;
import com.jacketus.RSOI_Lab2.Booksservice.storage.FileSystemStorageService;
import com.jacketus.RSOI_Lab2.Booksservice.storage.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class BooksServiceTest {
    private BooksService booksService;

    @Mock
    BooksRepository booksRepository;

    @Before
    public void setUp(){
        initMocks(this);
        StorageService s = new FileSystemStorageService();
        booksService = new BooksServiceImplementation(booksRepository, s);
    }

    @Test
    public void shouldCreateNewBook(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        

        try {
            given(booksRepository.save(book)).willReturn(book);
            Book booksaved = booksRepository.save(book);
            given(booksRepository.findById(booksaved.getId())).willReturn(Optional.of(booksaved));

            booksService.createBook(book);

            assertEquals("Author", booksaved.getAuthor());
            assertEquals("Book", booksaved.getName());
            
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void shouldReturnBooksList(){
        List<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        
        books.add(book);
        books.add(book);
        books.add(book);

        Page<Book> books_p = new PageImpl<Book>(books);

        given(booksRepository.findAll(PageRequest.of(1, 1))).willReturn(books_p);
        Page<Book> booksReturned = booksService.getAllBooks(PageRequest.of(1, 1));

        assertThat(booksReturned, is(books_p));
    }

    @Test
    public void shouldNotFindBook(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        


        given(booksRepository.findById(book.getId())).willReturn(Optional.of(book));
        Book booksaved = booksRepository.save(book);
/*
        boolean founded = true;
        try {
            booksService.getRateNum(book.getId() + 1);
        } catch (Exception ex) {
            founded = false;
        }

        assertEquals(false, founded);
*/
    }

    @Test
    public void shouldSetReviewsNum(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        /*
        book.setRating(5);

        try {
            given(booksRepository.save(book)).willReturn(book);
            Book booksaved = booksRepository.save(book);
            given(booksRepository.findById(booksaved.getId())).willReturn(Optional.of(booksaved));

            int revs_num = booksService.getRateNum(booksaved.getId());
            assertEquals(1, revs_num);
        }
        catch (BookNotFoundException ex){
            fail();
        }*/
    }

    @Test
    public void shouldAddRate(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        /*
        book.setRating(5);

        try {
            given(booksRepository.save(book)).willReturn(book);
            given(booksRepository.findById(book.getId())).willReturn(Optional.of(book));
            Book booksaved = booksRepository.save(book);

            booksService.setRating(booksaved.getId(), 3);
            double r = booksService.getRating(booksaved.getId());
            int eq = 0;
            if (r >= 0) eq = 1;
            assertEquals(1, eq);
        }
        catch (BookNotFoundException ex){
            fail();
        }*/
    }

    @Test
    public void shouldAddBuyNum(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        

        try {
            given(booksRepository.save(book)).willReturn(book);
            given(booksRepository.findById(book.getId())).willReturn(Optional.of(book));
            Book booksaved = booksRepository.save(book);

            booksService.incBuyNum(booksaved.getId());
            int r = booksService.getBuyNum(booksaved.getId());
            assertEquals(1, r);
        }
        catch (BookNotFoundException ex){
            fail();
        }
    }

    @Test
    public void shouldHash(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        /*
        book.setRating(5);

        try {
            given(booksRepository.save(book)).willReturn(book);
            given(booksRepository.findById(book.getId())).willReturn(Optional.of(book));
            Book booksaved = booksRepository.save(book);

            assertNotNull(booksaved.hashCode());
        }
        catch (Exception ex){
            fail();
        }*/
    }

    @Test
    public void shouldToString(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        /*
        book.setRating(5);
        String s = "Book {id=null, artist=Artist, name='Book', link=Link, rating=5.0, rate_nums=1, buy_nums=0}";

        try {
            given(booksRepository.save(book)).willReturn(book);
            given(booksRepository.findById(book.getId())).willReturn(Optional.of(book));
            Book booksaved = booksRepository.save(book);

            System.out.println(booksaved.toString());
            assertEquals(s, booksaved.toString());
        }
        catch (Exception ex){
            fail();
        }*/
    }

    @Test
    public void shouldEquals(){
        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        /*
        book.setRating(5);

        try {
            given(booksRepository.save(book)).willReturn(book);
            given(booksRepository.findById(book.getId())).willReturn(Optional.of(book));
            Book booksaved = booksRepository.save(book);

            boolean eq = book.equals(booksaved);
            assertEquals(true, eq);
        }
        catch (Exception ex){
            fail();
        }*/
    }

}
