package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Reading;
import com.rozangelapm.readmatch.model.ReadingStatus;
import org.springframework.stereotype.Service;

@Service
public class ReadingService {

    public Reading createReading(Book book){

        Reading newRead = new Reading();
        newRead.setBook(book);
        newRead.setStatus(ReadingStatus.WANT_TO_READ);

        return newRead;
    }

}
