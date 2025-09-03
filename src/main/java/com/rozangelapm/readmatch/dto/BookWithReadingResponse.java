package com.rozangelapm.readmatch.dto;

public record BookWithReadingResponse(
        String bookTitle,
        String author,
        ReadingResponse reading
) {}
