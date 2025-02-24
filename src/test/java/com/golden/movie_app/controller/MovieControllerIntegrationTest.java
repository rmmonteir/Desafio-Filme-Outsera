package com.golden.movie_app.controller;

import com.golden.movie_app.entity.Movie;
import com.golden.movie_app.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Test
    void testListAllMovies() throws Exception {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);
        movieRepository.save(movie);

        mockMvc.perform(get("/movie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Movie 1"));
    }

    @Test
    void testSearchMovieById() throws Exception {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);
        movie = movieRepository.save(movie);

        mockMvc.perform(get("/movie/" + movie.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Movie 1"));
    }

    @Test
    void testSearchMovieByIdNotFound() throws Exception {
        mockMvc.perform(get("/movie/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMovie() throws Exception {
        String movieJson = "{ \"release_year\": 2020, \"title\": \"Movie 1\", \"studio\": \"Studio 1\", \"producer\": \"Producer 1\", \"winner\": 1 }";

        mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Movie 1"));
    }

    @Test
    void testUpdateMovie() throws Exception {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);
        movie = movieRepository.save(movie);

        String updatedMovieJson = "{ \"release_year\": 2021, \"title\": \"Movie 1 Updated\", \"studio\": \"Studio 1 Updated\", \"producer\": \"Producer 1 Updated\", \"winner\": 0 }";

        mockMvc.perform(put("/movie/" + movie.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMovieJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Movie 1 Updated"));
    }

    @Test
    void testUpdateMovieNotFound() throws Exception {
        String updatedMovieJson = "{ \"release_year\": 2021, \"title\": \"Movie 1 Updated\", \"studio\": \"Studio 1 Updated\", \"producer\": \"Producer 1 Updated\", \"winner\": 0 }";

        mockMvc.perform(put("/movie/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMovieJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMovie() throws Exception {
        Movie movie = new Movie(2020, "Movie 1", "Studio 1", "Producer 1", 1);
        movie = movieRepository.save(movie);

        mockMvc.perform(delete("/movie/" + movie.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteMovieNotFound() throws Exception {
        mockMvc.perform(delete("/movie/999"))
                .andExpect(status().isNoContent());
    }
    @Test
    void testGetAwardIntervals() throws Exception {
        Movie movie1 = new Movie(2020, "Movie 1", "Studio 1", "Producer A", 1);
        Movie movie2 = new Movie(2021, "Movie 2", "Studio 2", "Producer A", 1);
        Movie movie3 = new Movie(2015, "Movie 3", "Studio 3", "Producer B", 1);
        Movie movie4 = new Movie(2020, "Movie 4", "Studio 4", "Producer B", 1);

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);
        movieRepository.save(movie4);

        mockMvc.perform(MockMvcRequestBuilders.get("/movie/intervalo-premiacao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min[0].producer").value("Producer A"))
                .andExpect(jsonPath("$.min[0].interval").value(1))
                .andExpect(jsonPath("$.min[0].previousWin").value(2020))
                .andExpect(jsonPath("$.min[0].followingWin").value(2021))
                .andExpect(jsonPath("$.max[0].producer").value("Producer B"))
                .andExpect(jsonPath("$.max[0].interval").value(5))
                .andExpect(jsonPath("$.max[0].previousWin").value(2015))
                .andExpect(jsonPath("$.max[0].followingWin").value(2020));
    }

    @Test
    void testGetAwardIntervalsWithNoWinners() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/movie/intervalo-premiacao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min").isEmpty())
                .andExpect(jsonPath("$.max").isEmpty());
    }
}