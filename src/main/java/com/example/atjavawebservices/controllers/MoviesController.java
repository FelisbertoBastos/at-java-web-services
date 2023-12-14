package com.example.atjavawebservices.controllers;

import com.example.atjavawebservices.dtos.MovieInfoDTO;
import com.example.atjavawebservices.dtos.ResponseDTO;
import com.example.atjavawebservices.exceptions.ResourceNotFoundException;
import com.example.atjavawebservices.models.Movie;
import com.example.atjavawebservices.services.MovieService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController {
    Logger logger = LoggerFactory.getLogger(MoviesController.class);

    @Autowired
    MovieService movieService;

    @GetMapping
    public List<Movie> getAll() {
        return movieService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "false") boolean ratings,
            @RequestParam(required = false, defaultValue = "false") boolean release) {
        try {
            MovieInfoDTO movieInfoDTO = movieService
                    .getById(id, ratings, release)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

            return ResponseEntity.ok(movieInfoDTO);
        } catch (ResourceNotFoundException ex) {
            ResponseDTO responseDTO = new ResponseDTO(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) {
        try {
            Movie movie = movieService.deleteById(id);
            return ResponseEntity.ok(movie);
        } catch (ResourceNotFoundException ex) {
            ResponseDTO responseDTO = new ResponseDTO(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Movie movie) {
        try {
            Movie createdMovie = movieService.create(movie);
            return ResponseEntity.ok().body(createdMovie);
        } catch (ResourceNotFoundException ex) {
            ResponseDTO responseDTO = new ResponseDTO(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody Movie movie) {
        if (id != movie.getId()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            Movie updatedMovie = movieService
                    .update(movie).orElseThrow(() -> new ResourceNotFoundException("Movie not found in OMDB"));
            return ResponseEntity.ok(updatedMovie);
        } catch (RuntimeException ex) {
            ResponseDTO responseDTO = new ResponseDTO(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
