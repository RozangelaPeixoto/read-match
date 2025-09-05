package com.rozangelapm.readmatch.dto;

import java.util.List;

public record RecommendationResponse(
        String idBook,
        String bookTitle,
        String author,
        List<String> genreNames
) {
}
