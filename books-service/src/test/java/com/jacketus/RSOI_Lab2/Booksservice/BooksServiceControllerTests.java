package com.jacketus.RSOI_Lab2.booksservice;

import com.jacketus.RSOI_Lab2.Booksservice.controller.BooksServiceController;
import com.jacketus.RSOI_Lab2.Booksservice.entity.Book;
import com.jacketus.RSOI_Lab2.Booksservice.repository.BooksRepository;
import com.jacketus.RSOI_Lab2.Booksservice.service.BooksService;
import com.jacketus.RSOI_Lab2.Booksservice.service.BooksServiceImplementation;
import com.jacketus.RSOI_Lab2.Booksservice.storage.FileSystemStorageService;
import com.jacketus.RSOI_Lab2.Booksservice.storage.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BooksServiceControllerTests {

	//@Autowired
    private MockMvc mvc;

	@MockBean
    private BooksServiceController booksServiceController;

	@Mock
	BooksService booksService;

	@Mock
    BooksRepository booksRepository;

    @Before
    public void setUp(){
        initMocks(this);

        StorageService s = new FileSystemStorageService(); booksService = new BooksServiceImplementation(booksRepository, s);
        booksServiceController = new BooksServiceController(booksService);
        mvc = MockMvcBuilders.standaloneSetup(booksServiceController).build();
    }

	@Test
	public void getAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        book.setDescription("Description");
        
        books.add(book);
        books.add(book);
        books.add(book);

        Page<Book> books_p = new PageImpl<Book>(books);

        given(booksServiceController.getAllBooks(1,5)).willReturn(books_p);
        mvc.perform(get("/books?page=1&size=5")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].name", is(book.getName())));
	}

    @Test
    public void getBookByID() throws Exception {
        List<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        
        books.add(book);

        given(booksRepository.findById(1L)).willReturn(Optional.of(book));
        mvc.perform(get("/books/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(book.getName())));
    }

    @Test
    public void addBook() throws Exception {
        List<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        
        books.add(book);

        mvc.perform(post("/books")
                .contentType("application/json")
                .content("{\"artist\":\"Sample Artist\", \"name\":\"Sample name\", \"link\":\"Sample Link\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void rateBook() throws Exception {
        List<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        
        books.add(book);

        given(booksRepository.findById(1L)).willReturn(Optional.of(book));
        mvc.perform(post("/books/1/rate")
                .contentType("application/json")
                .param("rating", "4"))
                .andExpect(status().isOk());
    }

    @Test
    public void buyBook() throws Exception {
        List<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setAuthor("Author");
        book.setName("Book");
        
        books.add(book);

        given(booksRepository.findById(1L)).willReturn(Optional.of(book));
        mvc.perform(post("/books/1/buy")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
