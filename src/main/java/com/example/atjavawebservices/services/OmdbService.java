package com.example.atjavawebservices.services;

import com.example.atjavawebservices.exceptions.ResourceNotFoundException;
import com.example.atjavawebservices.models.MovieExtras;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class OmdbService {
    Logger logger = LoggerFactory.getLogger(OmdbService.class);
    private final String API_KEY = "71f84c8c";

    public MovieExtras getMovieInfo(String title, int year) throws URISyntaxException {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String uri = String.format("https://www.omdbapi.com/?apikey=%s&t=%s&y=%s", API_KEY, encodedTitle, year);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(uri))
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("OMDB API status code: " + response.statusCode());

            if (response.statusCode() == 404) {
                throw new ResourceNotFoundException(response.body());
            }

            ObjectMapper mapper = JsonMapper.builder().build();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(response.body(), MovieExtras.class);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
