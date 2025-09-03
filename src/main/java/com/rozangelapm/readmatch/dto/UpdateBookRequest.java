package com.rozangelapm.readmatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateBookRequest(
        @NotBlank(message = "Título é obrigatório")
        String title,

        @NotBlank(message = "Autor é obrigatório")
        String author,

        @NotBlank(message = "Resumo é obrigatório")
        String description,

        @NotNull(message = "Gênero é obrigatório")
        @Size(min = 1, message = "É preciso ter pelo menos um gênero")
        List<String> genres,

        @NotBlank(message = "Editora é obrigatório")
        String publisher) {
}
