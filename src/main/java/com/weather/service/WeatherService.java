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
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final HttpClient client = HttpClient.newHttpClient();

    @Value("${weather.api.key}")
    private String apiKey;

    @Transactional(readOnly = true)
    public List<LocationDto> findLocationsByQuery(String searchQuery) {
        try {
            String encodedQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);

            String url = String.format(
                    "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%d&appid=%s",
                    encodedQuery, 10, apiKey
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<WeatherDto> getWeather(List<Location> locations) {
        List<WeatherDto> weathers = new ArrayList<>();
        for (Location location : locations) {
            try {
                String encodedLocation = URLEncoder.encode(location.getName(), StandardCharsets.UTF_8);

                String url = String.format(
                        "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
                        encodedLocation, apiKey
                );

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());

                String icon = root.path("weather").get(0).path("icon").asText();
                String country = root.path("sys").path("country").asText();
                double temperature = root.path("main").path("temp").asDouble();
                double feelsLike = root.path("main").path("feels_like").asDouble();
                String description = root.path("weather").get(0).path("description").asText();
                int humidity = root.path("main").path("humidity").asInt();

                weathers.add(new WeatherDto(
                        location.getId(),
                        location.getName(),
                        icon,
                        country,
                        (int) (temperature - 273.15),
                        (int) (feelsLike - 273.15),
                        description.substring(0, 1).toUpperCase() + description.substring(1),
                        humidity
                ));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return weathers;
    }
}
