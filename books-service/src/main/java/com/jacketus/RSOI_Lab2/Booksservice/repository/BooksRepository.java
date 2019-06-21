package com.jacketus.RSOI_Lab2.Booksservice.repository;

import com.jacketus.RSOI_Lab2.Booksservice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByAuthorContaining(String author);
    List<Book> findAllByNameContaining(String name);
    List<Book> findAllByDescriptionContaining(String description);
    List<Book> findAllByGenreContaining(String genre);
//    @Query(value = "SELECT r FROM BOOKS r WHERE "
//            + "r.author LIKE '%' || :searchString || '%' "
//            + "OR r.name LIKE '%' || :searchString || '%' "
//            + "OR r.description LIKE '%' || :searchString || '%' "
//            + "OR r.genre LIKE '%' || :searchString || '%' "
//    List<Book> searchBooks( @Param("authorString")String authorString, @Param("nameString") String nameString, @Param("descriptionString")String descriptionString, @Param("genreString")String genreString,             Pageable page);


}
