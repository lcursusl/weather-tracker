package com.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherDto(
        Long id,
        String name,
        String icon,
        String country,
        Integer temp,
        Integer feels_like,
        String description,
        Integer humidity
) {
}
