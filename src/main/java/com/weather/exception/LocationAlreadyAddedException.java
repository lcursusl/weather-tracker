package com.weather.exception;

public class LocationAlreadyAddedException extends RuntimeException {
    public LocationAlreadyAddedException(String message) {
        super(message);
    }
}
