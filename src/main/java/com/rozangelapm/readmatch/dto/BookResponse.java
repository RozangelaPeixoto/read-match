package com.rozangelapm.readmatch.dto;

import java.util.List;

public record BookResponse(
        String id,
        String title,
        String author,
        String description,
        List<String> genres,
        Integer publishedYear,
        String publisher,
        Integer pageCount) {
}
