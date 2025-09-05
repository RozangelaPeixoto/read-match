package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.dto.PreferenceResquest;
import com.rozangelapm.readmatch.dto.RecommendationResponse;
import com.rozangelapm.readmatch.mapper.BookMapper;
import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Genre;
import com.rozangelapm.readmatch.model.Preference;
import com.rozangelapm.readmatch.repository.BookRepository;
import com.rozangelapm.readmatch.repository.GenreRepository;
import com.rozangelapm.readmatch.repository.PreferenceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public PreferenceService(PreferenceRepository preferenceRepository, GenreRepository genreRepository, BookRepository bookRepository) {
        this.preferenceRepository = preferenceRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void savePreferences(PreferenceResquest preferenceDto) {

        for (String name : preferenceDto.genres()) {
            Genre genre = genreRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> genreRepository.save(new Genre(null, name)));

            preferenceRepository.findByGenre(genre).orElseGet(() -> {
                Preference p = new Preference(null, genre, 0.0, 0, 0.0);
                return preferenceRepository.save(p);
            });
        }
    }

    public List<RecommendationResponse> findRecommendations() {
        Pageable top3 = PageRequest.of(0, 3, Sort.by("avgRating").descending());
        List<Preference> preferences = preferenceRepository.findAll(top3).getContent();

        if (preferences.size() < 3) {
            return BookMapper.toRecommendationDtoList(bookRepository.findRandomUnreadBooks(3));
        }

        List<RecommendationResponse> recommendations = new ArrayList<>();

        for (Preference pref : preferences) {
            Optional<Book> book = bookRepository.findFirstUnreadBookByGenre(pref.getGenre().getId());
            book.ifPresent(b -> recommendations.add(BookMapper.toRecommendationDto(b)));
        }

        return recommendations;
    }

}
