package com.example.atjavawebservices.dtos;

import com.example.atjavawebservices.models.Movie;
import com.example.atjavawebservices.models.Rating;
import lombok.Data;

import java.util.List;

@Data
public class MovieInfoDTO {
    private Movie movie;
    private String releaseDate;
    private List<Rating> ratings;
}
