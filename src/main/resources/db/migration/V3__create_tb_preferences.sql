CREATE TABLE tb_preferences (
    id INT AUTO_INCREMENT PRIMARY KEY,
    genre_id INT NOT NULL UNIQUE,
    avg_rating DOUBLE NOT NULL,
    rating_count INT NOT NULL DEFAULT 0,
    FOREIGN KEY (genre_id) REFERENCES tb_genres(id) ON DELETE CASCADE
);