package com.golden.movie_app.service;

import com.golden.movie_app.dto.AwardInterval;
import com.golden.movie_app.dto.AwardIntervalResponse;
import com.golden.movie_app.entity.Movie;
import com.golden.movie_app.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MovieServiceIntegrationTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Test
    void testCreateMovie() {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);

        Movie savedMovie = movieService.createMovie(movie);

        assertNotNull(savedMovie.getId());
        assertEquals("Movie 1", savedMovie.getTitle());
    }

    @Test
    void testSearchMovieById() {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);
        movie = movieRepository.save(movie);

        Optional<Movie> foundMovie = movieService.searchMovieById(movie.getId());

        assertTrue(foundMovie.isPresent());
        assertEquals("Movie 1", foundMovie.get().getTitle());
    }

    @Test
    void testUpdateMovie() {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);
        movie = movieRepository.save(movie);

        Movie updatedMovie = new Movie(2021, "Movie 1 Updated", "Studio 1 Updated", "Producer 1 Updated", 0);

        Optional<Movie> result = movieService.updateMovie(movie.getId(), updatedMovie);

        assertTrue(result.isPresent());
        assertEquals("Movie 1 Updated", result.get().getTitle());
    }

    @Test
    void testDeleteMovie() {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);
        movie = movieRepository.save(movie);

        movieService.deleteMovie(movie.getId());

        assertFalse(movieRepository.findById(movie.getId()).isPresent());
    }
    @Test
    void testGetAwardIntervals() {
        Movie movie1 = new Movie(2020, "Movie 1", "Studio 1", "Producer A", 1);
        Movie movie2 = new Movie(2021, "Movie 2", "Studio 2", "Producer A", 1);
        Movie movie3 = new Movie(2015, "Movie 3", "Studio 3", "Producer B", 1);
        Movie movie4 = new Movie(2020, "Movie 4", "Studio 4", "Producer B", 1);

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);
        movieRepository.save(movie4);

        AwardIntervalResponse response = movieService.getAwardIntervals();

        List<AwardInterval> minIntervals = response.getMin();
        List<AwardInterval> maxIntervals = response.getMax();

        assertEquals(1, minIntervals.size());
        assertEquals("Producer A", minIntervals.get(0).getProducer());
        assertEquals(1, minIntervals.get(0).getInterval());
        assertEquals(2020, minIntervals.get(0).getPreviousWin());
        assertEquals(2021, minIntervals.get(0).getFollowingWin());

        assertEquals(1, maxIntervals.size());
        assertEquals("Producer B", maxIntervals.get(0).getProducer());
        assertEquals(5, maxIntervals.get(0).getInterval());
        assertEquals(2015, maxIntervals.get(0).getPreviousWin());
        assertEquals(2020, maxIntervals.get(0).getFollowingWin());
    }

    @Test
    void testGetAwardIntervalsWithNoWinners() {
        AwardIntervalResponse response = movieService.getAwardIntervals();

        assertTrue(response.getMin().isEmpty());
        assertTrue(response.getMax().isEmpty());
    }
}