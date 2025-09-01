CREATE TABLE tb_genres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tb_books (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description TEXT,
    published_year INT,
    publisher VARCHAR(100),
    page_count INT,
    id_google VARCHAR(50) UNIQUE,
    created_at DATETIME
);

CREATE TABLE tb_books_genres (
    book_id VARCHAR(36) NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (book_id, genre_id),
    FOREIGN KEY (book_id) REFERENCES tb_books(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES tb_genres(id) ON DELETE CASCADE
);