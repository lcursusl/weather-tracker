package com.weather.model.dto;

public record RegistrationRequest(String login, String password, String repeatPassword) {
}
