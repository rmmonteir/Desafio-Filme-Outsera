package com.golden.filme_app.controller;

import com.golden.filme_app.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filme")
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public ResponseEntity producer(){
        var allMovies = movieRepository.findAll();
        return ResponseEntity.ok(allMovies);
    }
}
