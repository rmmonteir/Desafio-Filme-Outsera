package com.golden.movie_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "filme")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer release_year;
    private String title;
    private String studio;
    private String producer;
    private Integer winner;

    public Movie(Integer release_year, String title, String studio, String producer, Integer winner) {
        this.release_year = release_year;
        this.title = title;
        this.studio = studio;
        this.producer = producer;
        this.winner = winner;
    }
}
