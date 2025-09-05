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

    @Transactional
    public void adjustPreferencesForRatingChange(String bookId, Double oldRating, Double newRating) {
        List<Genre> genres = bookRepository.findGenresByBookId(bookId);

        for (Genre genre : genres) {
            Preference pref = preferenceRepository.findByGenre(genre).orElseGet(() -> {
                Preference p = new Preference(null, genre, 0.0, 0, 0.0);
                return preferenceRepository.save(p);
            });

            double sum = pref.getRatingSum();
            int count = pref.getRatingCount();

            // caso 1: nota antiga < 4 e nova >= 4 (entrou no filtro)
            if (oldRating < 4.0 && newRating >= 4.0) {
                sum += newRating;
                count += 1;
            }
            // caso 2: nota antiga >= 4 e nova < 4 (saiu do filtro)
            else if (oldRating >= 4.0 && newRating < 4.0) {
                sum -= oldRating;
                count = Math.max(count - 1, 0);
            }
            // caso 3: ambas >= 4 (só atualizar soma)
            else if (oldRating >= 4.0 && newRating >= 4.0) {
                sum = sum - oldRating + newRating;
            }

            pref.setRatingSum(sum);
            pref.setRatingCount(count);
            pref.setAvgRating(count > 0 ? sum / count : 0.0);
            preferenceRepository.save(pref);
        }

    }

}
