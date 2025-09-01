package com.rozangelapm.readmatch.controller;

import com.rozangelapm.readmatch.dto.CreateBookRequest;
import com.rozangelapm.readmatch.dto.BookResponse;
import com.rozangelapm.readmatch.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody CreateBookRequest newBookDto){

        BookResponse bookSaved = bookService.saveBookFromGoogle(newBookDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookSaved.id())
                .toUri();

        return ResponseEntity.created(location).body(bookSaved);

    }

}
