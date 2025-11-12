package com.weather.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.model.dto.LocationDto;
import com.weather.model.dto.WeatherDto;
import com.weather.model.entity.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final HttpClient client = HttpClient.newHttpClient();

    @Value("${weather.api.key}")
    private String apiKey;

    @Transactional(readOnly = true)
    public List<LocationDto> findLocationsByQuery(String searchQuery) {
        String url = String.format(
                "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%d&appid=%s",
                searchQuery, 10, apiKey
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<WeatherDto> getWeather(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return new ArrayList<>();
        }

        List<WeatherDto> weathers = new ArrayList<>();
        for (Location location : locations) {
            String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
                    location.getName(), apiKey
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());

                weathers.add(new WeatherDto(
                        location.getId(),
                        location.getName(),
                        root.path("sys").path("country").asText(),
                        (root.path("main").path("temp").asInt() - 32) * 5 / 9,
                        (root.path("main").path("feels_like").asInt() - 32) * 5 / 9,
                        root.path("weather").path("description").asText(),
                        root.path("main").path("humidity").asInt()
                ));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return weathers;
    }
}
