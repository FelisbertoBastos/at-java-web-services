package com.example.atjavawebservices.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Min(value = 1900)
    private Integer year;
    private String director;
    private List<String> actors;
}
