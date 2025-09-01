package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
