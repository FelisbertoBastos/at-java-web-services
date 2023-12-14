package com.example.atjavawebservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MovieExtras {
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Released")
    private String released;

    @JsonProperty("Ratings")
    private List<Rating> ratings;
}
