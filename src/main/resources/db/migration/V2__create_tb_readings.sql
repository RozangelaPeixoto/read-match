CREATE TABLE tb_readings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id VARCHAR(36) NOT NULL UNIQUE,
    start_date DATE,
    end_date DATE,
    status ENUM('READ', 'READING', 'WANT_TO_READ', 'ABANDONED') NOT NULL,
    rating DOUBLE CHECK (rating >= 0 AND rating <= 5),
    CONSTRAINT fk_reading_book FOREIGN KEY (book_id) REFERENCES tb_books(id) ON DELETE CASCADE
);