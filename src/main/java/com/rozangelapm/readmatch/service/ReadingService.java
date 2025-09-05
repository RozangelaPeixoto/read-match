package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.dto.BookWithReadingResponse;
import com.rozangelapm.readmatch.dto.UpdateReadingResquest;
import com.rozangelapm.readmatch.mapper.ReadingMapper;
import com.rozangelapm.readmatch.model.*;
import com.rozangelapm.readmatch.repository.ReadingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReadingService {

    private final ReadingRepository readingRepository;
    private final PreferenceService preferenceService;

    public ReadingService(ReadingRepository readingRepository, PreferenceService preferenceService) {
        this.readingRepository = readingRepository;
        this.preferenceService = preferenceService;
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
                preferenceService.adjustPreferencesForRatingChange(bookId, reading.getRating(), updateReading.rating());
            }else{
                if (updateReading.rating() >= 4.0) {
                    preferenceService.adjustPreferencesForRatingChange(bookId, 0.0, updateReading.rating());
                }
            }
            reading.setRating(updateReading.rating());
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