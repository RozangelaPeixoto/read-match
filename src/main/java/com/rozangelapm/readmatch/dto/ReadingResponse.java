package com.rozangelapm.readmatch.dto;

import java.time.LocalDate;

public record ReadingResponse(
        LocalDate startDate,
        LocalDate endDate,
        String status,
        Double rating) {
}
