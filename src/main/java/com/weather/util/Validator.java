package com.weather.util;

import com.weather.exception.PasswordException;
import com.weather.exception.RepeatPasswordException;
import com.weather.exception.UsernameException;

public class Validator {
    public static void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new UsernameException("Username cannot be empty");
        } else if (username.length() > 64) {
            throw new UsernameException("Username is too long, it must be less than 65");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!username.matches(emailRegex)) {
            throw new UsernameException("Username must be a valid email address");
        }
    }

    public static void validatePassword(String password, String repeatPassword) {
        if (password == null || password.isBlank()) {
            throw new PasswordException("Password cannot be empty");
        } else if (!password.equals(repeatPassword)) {
            throw new RepeatPasswordException("Passwords do not match");
        } else if (password.length() > 255) {
            throw new PasswordException("Password is too long, it must be less than 256");
        }
    }
}
