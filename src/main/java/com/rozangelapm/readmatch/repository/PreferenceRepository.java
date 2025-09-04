package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Genre;
import com.rozangelapm.readmatch.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

    Optional<Preference> findByGenre(Genre genre);
}
