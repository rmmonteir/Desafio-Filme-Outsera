package com.golden.movie_app.controller;

import com.golden.movie_app.dto.AwardIntervalResponse;
import com.golden.movie_app.entity.Movie;
import com.golden.movie_app.exception.ProcessingException;
import com.golden.movie_app.service.ImportCsvService;
import com.golden.movie_app.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    ImportCsvService importCsvService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<Movie>> listAllMovies() {
        List<Movie> movies = movieService.listAllMovies();
        return ResponseEntity.ok(movies); // Status 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> searchMovieById(@PathVariable Long id) {
        return movieService.searchMovieById(id)
                .map(ResponseEntity::ok) // Status 200 OK
                .orElse(ResponseEntity.notFound().build()); // Status 404 Not Found
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie Movie) {
        Movie movie = movieService.createMovie(Movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(movie); // Status 201 Created
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie Movie) {
        return movieService.updateMovie(id, Movie)
                .map(ResponseEntity::ok) // Status 200 OK
                .orElse(ResponseEntity.notFound().build()); // Status 404 Not Found
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build(); // Status 204 No Content
    }

    @PostMapping("/importar-csv")
    public ResponseEntity uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            importCsvService.csvLoadProcessing(file);
            return ResponseEntity.ok(messageSource.getMessage("csv.file.ok",null, null));
        } catch (Exception e) {
            throw new ProcessingException(messageSource.getMessage("error.csv.file.processing",new Object[]{e.getMessage()}, null) , e);
        }
    }
    @GetMapping("/intervalo-premiacao")
    public ResponseEntity<AwardIntervalResponse> getAwardIntervals() {
        AwardIntervalResponse response = movieService.getAwardIntervals();
        return ResponseEntity.ok(response);
    }
}
