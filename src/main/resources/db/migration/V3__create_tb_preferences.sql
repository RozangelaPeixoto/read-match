CREATE TABLE tb_preferences (
    id INT AUTO_INCREMENT PRIMARY KEY,
    genre_id INT NOT NULL UNIQUE,
    rating_sum DOUBLE NOT NULL DEFAULT 0,
    rating_count INT NOT NULL DEFAULT 0,
    avg_rating DOUBLE NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES tb_genres(id) ON DELETE CASCADE
);