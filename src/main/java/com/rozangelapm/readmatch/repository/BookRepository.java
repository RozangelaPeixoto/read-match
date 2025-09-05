package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String>{

    boolean existsByIdGoogle(String id);

    List<Book> findByGenres(Genre genre);

    @Query("SELECT g FROM Book b JOIN b.genres g WHERE b.id = :bookId")
    List<Genre> findGenresByBookId(@Param("bookId") String bookId);

}
