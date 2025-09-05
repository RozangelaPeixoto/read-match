package com.rozangelapm.readmatch.mapper;

import com.rozangelapm.readmatch.dto.BookResponse;
import com.rozangelapm.readmatch.dto.RecommendationResponse;
import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {

    public static BookResponse toDto(Book book){
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getGenres().stream().map(Genre::getName).toList(),
                book.getPublishedYear(),
                book.getPublisher(),
                book.getPageCount());
    }

    public static List<BookResponse> toDtoList(List<Book> books){
        if (books == null) {
            return null;
        }

        List<BookResponse> bookList = new ArrayList<BookResponse>(books.size());
        for (Book book : books) {
            bookList.add( toDto(book) );
        }

        return bookList;
    }

    public static RecommendationResponse toRecommendationDto(Book book){
        return new RecommendationResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenres()
                        .stream()
                        .map(Genre::getName)
                        .toList());
    }

    public static List<RecommendationResponse> toRecommendationDtoList(List<Book> books){
        if (books == null) {
            return null;
        }

        List<RecommendationResponse> bookList = new ArrayList<RecommendationResponse>(books.size());
        for (Book book : books) {
            bookList.add( toRecommendationDto(book) );
        }

        return bookList;
    }
}
