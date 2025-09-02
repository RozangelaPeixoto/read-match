package com.rozangelapm.readmatch.controller;

import com.rozangelapm.readmatch.dto.CreateBookRequest;
import com.rozangelapm.readmatch.dto.BookResponse;
import com.rozangelapm.readmatch.dto.UpdateBookRequest;
import com.rozangelapm.readmatch.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponse> postBook(@Valid @RequestBody CreateBookRequest newBookDto){

        BookResponse bookSaved = bookService.saveBookFromGoogle(newBookDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookSaved.id())
                .toUri();

        return ResponseEntity.created(location).body(bookSaved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("id") String id){

        BookResponse book = bookService.getBookById(id);
        if(book == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(
            @RequestParam(name = "genero", required = false) String genre){

        if(genre != null){ return ResponseEntity.ok(bookService.getAllBooksByGenre(genre)); }

        return ResponseEntity.ok(bookService.getAllBooks());

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable("id") String id,
                                           @RequestBody UpdateBookRequest updateDto){

        if(bookService.updateBookById(id, updateDto)) return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") String id){

        if(bookService.deleteBookById(id)) return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

}
