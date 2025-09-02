package com.rozangelapm.readmatch.dto;

import java.util.List;

public record UpdateBookRequest(
        String title,
        String author,
        String description,
        List<String> genres,
        String publisher) {
}
