package com.example.atjavawebservices.services;

import com.example.atjavawebservices.dtos.MovieInfoDTO;
import com.example.atjavawebservices.exceptions.ResourceNotFoundException;
import com.example.atjavawebservices.models.Movie;
import com.example.atjavawebservices.models.MovieExtras;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.*;

@Service
public class MovieService {
    Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    OmdbService omdbService;

    private int currentKey = 0;
    private final Map<Integer, Movie> movies = initMovies();

    private Map<Integer, Movie> initMovies() {
        return new HashMap<Integer, Movie>();
    }

    public List<Movie> getAll() {
        return movies.values().stream().toList();
    }

    public Optional<MovieInfoDTO> getById(int id, boolean withRating, boolean withReleaseDate) {
        Movie movie = movies.get(id);

        if (movie == null) return Optional.empty();

        MovieInfoDTO movieInfoDTO = new MovieInfoDTO();
        movieInfoDTO.setMovie(movie);

        if (withRating || withReleaseDate) {
            try {
                MovieExtras movieInfo = omdbService.getMovieInfo(movie.getName(), movie.getYear());
                if (withRating) movieInfoDTO.setRatings(movieInfo.getRatings());
                if (withReleaseDate) movieInfoDTO.setReleaseDate(movieInfo.getReleased());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.of(movieInfoDTO);
    }

    public Movie deleteById(int id) {
        if (!movies.containsKey(id)) throw new ResourceNotFoundException("Movie not found");
        return movies.remove(id);
    }

    public Movie create(Movie movie) {
        MovieExtras movieInfo = null;
        try {
            movieInfo = omdbService.getMovieInfo(movie.getName(), movie.getYear());
            logger.info("movieExtras: " + movieInfo);

            if (movie.getName().equalsIgnoreCase(movieInfo.getTitle())) {
                currentKey += 1;
                movie.setId(currentKey);
                movie.setName(movieInfo.getTitle());
                movies.put(currentKey, movie);
                return movie;
            } else {
                throw new ResourceNotFoundException("Movie not found in OMDB");
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Movie> update(Movie movie) {
        if (!movies.containsKey(movie.getId())) throw new ResourceNotFoundException("Movie not found");
        try {
            MovieExtras movieInfo = omdbService.getMovieInfo(movie.getName(), movie.getYear());

            if (movie.getName().equalsIgnoreCase(movieInfo.getTitle())) {
                movies.put(movie.getId(), movie);
                return Optional.of(movie);
            } else {
                return Optional.empty();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearMovies() {
        movies.clear();
        currentKey = 0;
    }
}
