package com.weather.model.dto;

public record LocationDto(
        String name,
        Double lat,
        Double lon,
        String country,
        String state) {
}
