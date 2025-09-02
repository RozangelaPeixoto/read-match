package com.rozangelapm.readmatch.service;


import com.rozangelapm.readmatch.dto.GoogleBookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.google-books.url}")
    private String apiUrl;

    public GoogleBookResponse getBookById(String googleId) {
        String url = apiUrl + googleId;
        return restTemplate.getForObject(url, GoogleBookResponse.class);
    }


}
