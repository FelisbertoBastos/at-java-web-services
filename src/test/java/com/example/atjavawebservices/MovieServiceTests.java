package com.example.atjavawebservices;

import com.example.atjavawebservices.exceptions.ResourceNotFoundException;
import com.example.atjavawebservices.models.Movie;
import com.example.atjavawebservices.services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MovieServiceTests {
    @Autowired
    MovieService movieService;

    @BeforeEach
    public void beforeEach() {
        movieService.clearMovies();
    }

    @Test
    public void getAllTest() {
        List<Movie> movies = movieService.getAll();
        assertEquals(0, movies.size());
    }

    @Test
    public void createTest() {
        Movie movie = new Movie();
        movie.setName("Titanic");
        movie.setYear(1997);
        Movie createdMovie = movieService.create(movie);
        assertEquals("Titanic", createdMovie.getName());
    }

    @Test
    public void updateTest() {
        Movie movie = new Movie();
        movie.setName("Titanic");
        movie.setYear(1997);
        movieService.create(movie);

        Movie movie2 = new Movie();
        movie2.setId(1);
        movie2.setName("The Godfather");
        movie2.setYear(1972);
        Optional<Movie> updatedMovie = movieService.update(movie2);
        assertTrue(updatedMovie.isPresent());
    }

    @Test
    public void deleteTest() {
        Movie movie = new Movie();
        movie.setName("Titanic");
        movie.setYear(1997);
        Movie createdMovie = movieService.create(movie);

        Movie deletedMovie = movieService.deleteById(1);
        assertEquals(createdMovie.getName(), deletedMovie.getName());

        List<Movie> movies = movieService.getAll();
        assertEquals(0, movies.size());
    }

    @Test
    public void deleteNotFoundTest() {
        assertThrows(ResourceNotFoundException.class, () -> movieService.deleteById(-1));
    }
}
