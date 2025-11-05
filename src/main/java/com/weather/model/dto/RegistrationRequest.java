package com.weather.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @Email(message = "Username must be a valid email address")
        @NotBlank(message = "Username cannot be empty")
        @Size(max = 64, message = "Username must be less than 65 characters")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Username must be a valid email address")
        String login,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String password,

        @NotBlank(message = "Please repeat your password")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String repeatPassword) {
}
