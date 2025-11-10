package com.weather.model.dto;

public record WeatherDto(
        Long id,
        String name,
        String country,
        Integer temp,
        Integer feels_like,
        String description,
        Integer humidity
) {
}
