package com.rozangelapm.readmatch.service;


import com.rozangelapm.readmatch.dto.GoogleBookResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes/";

    public GoogleBookResponse getBookById(String googleId) {
        String url = API_URL + googleId;
        return restTemplate.getForObject(url, GoogleBookResponse.class);
    }


}
