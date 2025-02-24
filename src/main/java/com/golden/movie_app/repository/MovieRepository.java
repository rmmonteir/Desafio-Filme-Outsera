package com.golden.movie_app.repository;

import com.golden.movie_app.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>{
    List<Movie> findByWinner(Integer winner);
}
