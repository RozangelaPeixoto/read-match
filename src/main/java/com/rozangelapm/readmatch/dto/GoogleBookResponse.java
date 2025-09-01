package com.rozangelapm.readmatch.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleBookResponse {
    private String id;
    private VolumeInfo volumeInfo;

    @Data
    public static class VolumeInfo {
        private String title;
        private List<String> authors;
        private String publisher;
        private String publishedDate;
        private String description;
        private Integer pageCount;
    }

}

