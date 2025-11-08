package com.weather.model.dto;

public record LocationDto(
        String name,
        Double latitude,
        Double longitude,
        String country,
        String state) {
}
