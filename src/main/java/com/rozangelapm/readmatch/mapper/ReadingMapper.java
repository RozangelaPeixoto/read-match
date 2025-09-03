package com.rozangelapm.readmatch.mapper;

import com.rozangelapm.readmatch.dto.BookWithReadingResponse;
import com.rozangelapm.readmatch.dto.ReadingResponse;
import com.rozangelapm.readmatch.model.Reading;

import java.util.ArrayList;
import java.util.List;

public class ReadingMapper {

    public static BookWithReadingResponse toDto(Reading reading){

        ReadingResponse readingResponse = new ReadingResponse(
                reading.getStartDate(),
                reading.getEndDate(),
                reading.getStatus().getLabel(),
                reading.getRating()
        );

        return new BookWithReadingResponse(reading.getBook().getTitle(), reading.getBook().getAuthor(), readingResponse);
    }

    public static List<BookWithReadingResponse> toDtoList(List<Reading> readings){
        if (readings == null) {
            return null;
        }

        List<BookWithReadingResponse> readingList = new ArrayList<BookWithReadingResponse>(readings.size());
        for (Reading reading : readings) {
            readingList.add( toDto(reading) );
        }

        return readingList;
    }
}