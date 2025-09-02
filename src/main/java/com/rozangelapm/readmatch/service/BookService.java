package com.rozangelapm.readmatch.service;

import com.rozangelapm.readmatch.dto.CreateBookRequest;
import com.rozangelapm.readmatch.dto.BookResponse;
import com.rozangelapm.readmatch.dto.GoogleBookResponse;
import com.rozangelapm.readmatch.dto.UpdateBookRequest;
import com.rozangelapm.readmatch.exception.DuplicateBookException;
import com.rozangelapm.readmatch.mapper.BookMapper;
import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Genre;
import com.rozangelapm.readmatch.repository.BookRepository;
import com.rozangelapm.readmatch.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return googleBooksService.getBookById(idGoogle);

    }

    public BookResponse getBookById(String id){

        Book book = bookRepository.findById(id).orElse(null);
        if(book == null) return null;
        return BookMapper.toDto(book);
    }

    public List<BookResponse> getAllBooks(){

        List<Book> listBooks = bookRepository.findAll();

        return BookMapper.toDtoList(listBooks);

    }

    public List<BookResponse> getAllBooksByGenre(String genreName){

        Genre genre = genreRepository.findByNameIgnoreCase(genreName).orElse(null);

        List<Book> listBooks = bookRepository.findByGenres(genre);

        return BookMapper.toDtoList(listBooks);

    }

    @Transactional
    public boolean updateBookById(String id, UpdateBookRequest bookDto){

        Book book = bookRepository.findById(id).orElse(null);
        if(book == null){ return false; }


        if(bookDto.title() != null) book.setTitle(bookDto.title());
        if(bookDto.author() != null) book.setAuthor(bookDto.author());
        if(bookDto.description() != null) book.setDescription(bookDto.description());
        if(bookDto.genres() != null) book.setGenres(bookDto.genres().stream()
                .map(name -> genreRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> genreRepository.save(new Genre(null, name))))
                .toList());
        if(bookDto.publisher() != null) book.setPublisher(bookDto.publisher());

        return true;
    }

    @Transactional
    public boolean deleteBookById(String id){

        if(bookRepository.existsById(id)){
            bookRepository.deleteById(id);
            return true;
        }

        return false;
    }

}
