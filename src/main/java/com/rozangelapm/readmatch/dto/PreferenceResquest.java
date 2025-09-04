package com.rozangelapm.readmatch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PreferenceResquest(
        @NotNull(message = "Gênero é obrigatório")
        @Size(min = 1, message = "É preciso ter pelo menos um gênero")
        List<String> genres
) {
}
