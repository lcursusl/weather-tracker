package com.weather.exception;

public class RepeatPasswordException extends RuntimeException {
    public RepeatPasswordException(String message) {
        super(message);
    }
}
