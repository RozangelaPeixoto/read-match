package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.dto.CreateBookRequest;
import com.rozangelapm.readmatch.dto.BookResponse;
import com.rozangelapm.readmatch.dto.GoogleBookResponse;
import com.rozangelapm.readmatch.exception.DuplicateBookException;
import com.rozangelapm.readmatch.mapper.BookMapper;
import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Genre;
import com.rozangelapm.readmatch.repository.BookRepository;
import com.rozangelapm.readmatch.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final GoogleBooksService googleBooksService;
    private final GenreRepository genreRepository;

    public BookService(BookRepository bookRepository, GoogleBooksService googleBooksService, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.googleBooksService = googleBooksService;
        this.genreRepository = genreRepository;
    }

    @Transactional
    public BookResponse saveBookFromGoogle(CreateBookRequest newBookDto){

        String idGoogle = newBookDto.idGoogle();

        System.out.println(bookRepository.existsByIdGoogle(idGoogle));

        if(bookRepository.existsByIdGoogle(idGoogle)){
            throw new DuplicateBookException("Esse livro já foi cadastrado");
        }

        GoogleBookResponse response = getResponseGoogle(idGoogle);

        Book book = new Book();
        book.setTitle(response.getVolumeInfo().getTitle());
        book.setAuthor(
                response.getVolumeInfo().getAuthors() != null ?
                        String.join(", ", response.getVolumeInfo().getAuthors()) : null
        );
        book.setPublisher(response.getVolumeInfo().getPublisher());
        book.setPublishedYear(
                Optional.ofNullable(response.getVolumeInfo().getPublishedDate())
                        .map(date -> Integer.valueOf(date.substring(0, 4)))
                        .orElse(null)
        );
        book.setDescription(response.getVolumeInfo().getDescription());
        book.setPageCount(response.getVolumeInfo().getPageCount());

        List<Genre> genresEntities = newBookDto.genres().stream()
                .map(name -> genreRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> genreRepository.save(new Genre(null, name))))
                .toList();

        book.setGenres(genresEntities);
        book.setCreatedAt(LocalDateTime.now());
        book.setIdGoogle(response.getId());

        Book savedBook = bookRepository.save(book);

        return BookMapper.toDto(savedBook);

    }

    private GoogleBookResponse getResponseGoogle(String idGoogle) {

        GoogleBookResponse response = googleBooksService.getBookById(idGoogle);

        return response;

    }
}
