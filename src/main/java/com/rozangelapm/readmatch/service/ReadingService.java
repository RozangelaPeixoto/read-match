package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.dto.UpdateReadingResquest;
import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Reading;
import com.rozangelapm.readmatch.model.ReadingStatus;
import com.rozangelapm.readmatch.repository.BookRepository;
import com.rozangelapm.readmatch.repository.ReadingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReadingService {

    private final ReadingRepository readingRepository;

    public ReadingService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public Reading createReading(Book book){

        Reading newRead = new Reading();
        newRead.setBook(book);
        newRead.setStatus(ReadingStatus.WANT_TO_READ);

        return newRead;
    }

    @Transactional
    public void updateReading(String id, UpdateReadingResquest updateReading){

        Reading reading = readingRepository.findByBook_Id(id)
                .orElseThrow(() -> new EntityNotFoundException("Leitura do livro com id: " + id));

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
            if(updateReading.rating() < 0 || updateReading.rating() > 5){
                throw new IllegalArgumentException("A nota deve ser entre 0 e 5");
            }
            reading.setRating(updateReading.rating());
        }

    }

}
