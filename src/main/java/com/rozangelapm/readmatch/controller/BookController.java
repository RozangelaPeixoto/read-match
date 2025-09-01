package com.rozangelapm.readmatch.controller;

import com.rozangelapm.readmatch.dto.BookRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @PostMapping
    public ResponseEntity<String> createBook(@Valid @RequestBody BookRequest newBookDto){

        System.out.println(newBookDto.idGoogle());
        System.out.println(newBookDto.genres());

        return ResponseEntity.ok("Criado com sucesso!");
    }

}
