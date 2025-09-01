package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findByNameIgnoreCase(String name);

}
