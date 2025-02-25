package com.golden.movie_app.service;

import com.golden.movie_app.dto.AwardInterval;
import com.golden.movie_app.dto.AwardIntervalResponse;
import com.golden.movie_app.entity.Movie;
import com.golden.movie_app.repository.MovieRepository;
import com.golden.movie_app.util.Contants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> listAllMovies() {
        return movieRepository.findAll();
    }
    public Optional<Movie> searchMovieById(Long id) {
        return movieRepository.findById(id);
    }
    public Movie createMovie(Movie Movie) {
        return movieRepository.save(Movie);
    }

    public Optional<Movie> updateMovie(Long id, Movie Movie) {
        return movieRepository.findById(id)
                .map(existingMovie -> {
                    existingMovie.setRelease_year(Movie.getRelease_year());
                    existingMovie.setTitle(Movie.getTitle());
                    existingMovie.setStudio(Movie.getStudio());
                    existingMovie.setProducer(Movie.getProducer());
                    existingMovie.setWinner(Movie.getWinner());
                    return movieRepository.save(existingMovie);
                });
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public AwardIntervalResponse getAwardIntervals() {
        // Busca todos os filmes vencedores
        List<Movie> winningMovies = movieRepository.findByWinner(Contants.WINNER);
        // Mapa para armazenar os intervalos de cada produtor
        Map<String, List<Integer>> awardsByProducer = new HashMap<>();

        // Agrupa os anos de premiação por produtor
        for (Movie movie : winningMovies) {
            String[] producers = movie.getProducer().split(",| and ");
            for (String producer : producers) {
                producer = producer.trim();
                awardsByProducer.computeIfAbsent(producer, k -> new ArrayList<>()).add(movie.getRelease_year());
            }
        }

        // Lista para armazenar todos os intervalos
        List<AwardInterval> intervals = new ArrayList<>();

        // Calcula os intervalos para cada produtor
        for (Map.Entry<String, List<Integer>> entry : awardsByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            years.sort(Integer::compareTo);

            for (int i = 1; i < years.size(); i++) {
                int previousWin = years.get(i - 1);
                int followingWin = years.get(i);
                int interval = followingWin - previousWin;

                intervals.add(new AwardInterval(producer, interval, previousWin, followingWin));
            }
        }
        // Encontra os intervalos mínimos e máximos
        if (intervals.isEmpty()) {
            return new AwardIntervalResponse(new ArrayList<>(), new ArrayList<>());
        }

        int minInterval = intervals.stream().mapToInt(AwardInterval::getInterval).min().orElse(0);
        int maxInterval = intervals.stream().mapToInt(AwardInterval::getInterval).max().orElse(0);

        List<AwardInterval> minIntervals = intervals.stream()
                .filter(i -> i.getInterval() == minInterval)
                .collect(Collectors.toList());

        List<AwardInterval> maxIntervals = intervals.stream()
                .filter(i -> i.getInterval() == maxInterval)
                .collect(Collectors.toList());

        return new AwardIntervalResponse(minIntervals, maxIntervals);
    }
}