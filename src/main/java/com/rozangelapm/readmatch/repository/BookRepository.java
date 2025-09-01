package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String>{

    boolean existsByIdGoogle(String id);

}
