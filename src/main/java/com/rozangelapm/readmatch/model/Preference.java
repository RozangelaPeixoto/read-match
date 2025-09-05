package com.rozangelapm.readmatch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_preferences")
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "genre_id", nullable = false, unique = true)
    private Genre genre;

    @Column(nullable = false)
    private Double ratingSum;

    @Column(nullable = false)
    private Integer ratingCount;

    @Column(nullable = false)
    private Double avgRating;
}
