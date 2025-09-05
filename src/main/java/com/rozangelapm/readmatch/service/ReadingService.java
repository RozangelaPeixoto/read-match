package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.dto.BookWithReadingResponse;
import com.rozangelapm.readmatch.dto.UpdateReadingResquest;
import com.rozangelapm.readmatch.mapper.ReadingMapper;
import com.rozangelapm.readmatch.model.*;
import com.rozangelapm.readmatch.repository.BookRepository;
import com.rozangelapm.readmatch.repository.PreferenceRepository;
import com.rozangelapm.readmatch.repository.ReadingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReadingService {

    private final ReadingRepository readingRepository;
    private final BookRepository bookRepository;
    private final PreferenceRepository preferenceRepository;

    public ReadingService(ReadingRepository readingRepository, BookRepository bookRepository, PreferenceRepository preferenceRepository) {
        this.readingRepository = readingRepository;
        this.bookRepository = bookRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public Reading createReading(Book book){

        Reading newRead = new Reading();
        newRead.setBook(book);
        newRead.setStatus(ReadingStatus.WANT_TO_READ);

        return newRead;
    }

    @Transactional
    public void updateReading(String bookId, UpdateReadingResquest updateReading){

        Reading reading = readingRepository.findByBook_Id(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Leitura do livro com id: " + bookId));

        if (updateReading.status() != null) {
            reading.setStatus(ReadingStatus.fromLabel(updateReading.status()));
        }
        if (updateReading.startDate() != null) {
            reading.setStartDate(LocalDate.parse(updateReading.startDate()));
        }
        if (updateReading.endDate() != null) {
            reading.setEndDate(LocalDate.parse(updateReading.endDate()));
        }
        if (updateReading.rating() != null) {
            if(reading.getRating() != null){
                adjustPreferencesForRatingChange(bookId, reading.getRating(), updateReading.rating());
            }else{
                if (updateReading.rating() >= 4.0) {
                    adjustPreferencesForRatingChange(bookId, 0.0, updateReading.rating());
                }//updatePreferencesOnRating(bookId, updateReading.rating());
            }
            reading.setRating(updateReading.rating());
        }
    }

    @Transactional
    private void adjustPreferencesForRatingChange(String bookId, Double oldRating, Double newRating) {
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
                count -= 1;
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

    @Transactional
    private void updatePreferencesOnRating(String bookId, double rating) {
        if (rating < 4.0) {return;}

        List<Genre> genres = bookRepository.findGenresByBookId(bookId);

        for (Genre genre : genres) {
            Preference pref = preferenceRepository.findByGenre(genre).orElseGet(() -> {
                Preference p = new Preference(null, genre, 0.0, 0, 0.0);
                return preferenceRepository.save(p);
            });

            double sum = pref.getRatingSum() + rating;
            int count = pref.getRatingCount() + 1;

            pref.setRatingSum(sum);
            pref.setRatingCount(count);
            pref.setAvgRating(sum / count);

            preferenceRepository.save(pref);
        }
    }

    public BookWithReadingResponse findReadingById(String id){

        Reading reading = readingRepository.findByBook_Id(id)
                .orElseThrow(() -> new EntityNotFoundException("Leitura do livro com id: " + id));

        return ReadingMapper.toDto(reading);

    }

    public List<BookWithReadingResponse> findAllReadings(String status){

        if(status != null){
            ReadingStatus readingStatus = ReadingStatus.fromLabel(status);
            List<Reading> readingList = readingRepository.findByStatus(readingStatus);
            return ReadingMapper.toDtoList(readingList);
        }

        List<Reading> readingList = readingRepository.findAll();
        return ReadingMapper.toDtoList(readingList);
    }

}