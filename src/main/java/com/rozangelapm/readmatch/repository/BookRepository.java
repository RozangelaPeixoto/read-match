package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Book;
import com.rozangelapm.readmatch.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String>{

    boolean existsByIdGoogle(String id);

    List<Book> findByGenres(Genre genre);

    @Query("SELECT g FROM Book b JOIN b.genres g WHERE b.id = :bookId")
    List<Genre> findGenresByBookId(@Param("bookId") String bookId);

    @Query(value = """
    SELECT * FROM tb_books b
    WHERE b.id NOT IN (
        SELECT r.book_id FROM tb_readings r WHERE r.status = 'READ'
    )
    ORDER BY RAND()
    LIMIT :limit
    """, nativeQuery = true)
    List<Book> findRandomUnreadBooks(@Param("limit") int limit);

    @Query(value = """
    SELECT * FROM tb_books b
    JOIN tb_books_genres bg ON b.id = bg.book_id
    WHERE bg.genre_id = :genreId
    AND b.id NOT IN (
        SELECT r.book_id FROM tb_readings r WHERE r.status = 'READ'
    )
    ORDER BY RAND()
    LIMIT 1
    """, nativeQuery = true)
    Optional<Book> findFirstUnreadBookByGenre(@Param("genreId") Integer genreId);


}
