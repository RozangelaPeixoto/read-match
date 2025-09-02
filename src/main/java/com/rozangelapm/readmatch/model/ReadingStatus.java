package com.rozangelapm.readmatch.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReadingStatus {

    READ("lido"),
    WANT_TO_READ("quero ler"),
    ABANDONED("abandonado"),
    READING("lendo");

    private final String label;

    ReadingStatus(String label) {
        this.label = label;
    }

    public static ReadingStatus fromLabel(String label) {
        return Arrays.stream(ReadingStatus.values())
                .filter(status -> status.getLabel().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status permitidos: lendo, lido, quero ler e abandonado"));
    }
}
