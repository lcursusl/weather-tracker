package com.weather.model.dto;

import java.math.BigDecimal;

public record LocationDto(
        String name,
        BigDecimal lat,
        BigDecimal lon,
        String country,
        String state) {
}
