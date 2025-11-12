package com.weather.service;

import com.weather.exception.UserNotFoundException;
import com.weather.model.dto.LocationDto;
import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import com.weather.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void addLocation(LocationDto locationDto, Optional<User> user) {
        if (user.isEmpty()) {
            throw new UserNotFoundException("Log in or register before you can add a location");
        }
        locationRepository.save(locationDto, user.get());
    }

    public List<Location> getUserLocations(Optional<User> user) {
        if (user.isEmpty()) {
            return new ArrayList<>();
        }
        return locationRepository.findByUser(user.get());
    }

    public void deleteLocation(Long id, Optional<User> user) {
        if (user.isEmpty()) {
            throw new UserNotFoundException("Log in or register before you can add a location");
        }
        locationRepository.deleteByIdAndUser(id, user.get());
    }
}
