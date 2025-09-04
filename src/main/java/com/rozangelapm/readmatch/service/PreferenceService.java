package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.dto.PreferenceResquest;
import com.rozangelapm.readmatch.model.Genre;
import com.rozangelapm.readmatch.model.Preference;
import com.rozangelapm.readmatch.repository.GenreRepository;
import com.rozangelapm.readmatch.repository.PreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final GenreRepository genreRepository;

    public PreferenceService(PreferenceRepository preferenceRepository, GenreRepository genreRepository) {
        this.preferenceRepository = preferenceRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    public void savePreferences(PreferenceResquest preferenceDto) {

        for (String name : preferenceDto.genres()) {
            Genre genre = genreRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> genreRepository.save(new Genre(null, name)));

            preferenceRepository.findByGenre(genre).orElseGet(() -> {
                Preference p = new Preference(null, genre, 0.0, 0);
                return preferenceRepository.save(p);
            });
        }

    }
}
