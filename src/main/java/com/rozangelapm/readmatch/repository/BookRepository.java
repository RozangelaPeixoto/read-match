package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String>{

    boolean existsByIdGoogle(String id);

    List<Book> findByGenres(Genre genre);

}
