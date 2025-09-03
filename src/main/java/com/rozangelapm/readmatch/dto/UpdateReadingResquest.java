package com.rozangelapm.readmatch.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

public record UpdateReadingResquest(
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "Data de início deve estar no formato aaaa-mm-dd"
        )
        String startDate,

        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "Data de término deve estar no formato aaaa-mm-dd"
        )
        String endDate,

        @Pattern(
                regexp = "^(lido|lendo|quero ler|abandonado)$",
                message = "Status deve ser um dos valores: lido, lendo, quero ler, abandonado"
        )
        String status,

        @DecimalMin(value = "0.0", message = "Nota deve ser no mínimo 0")
        @DecimalMax(value = "5.0", message = "Nota deve ser no máximo 5")
        Double rating) {
}
