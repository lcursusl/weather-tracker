package com.weather.service;

import com.weather.exception.LocationAlreadyAddedException;
import com.weather.exception.UserNotFoundException;
import com.weather.model.dto.LocationDto;
import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import com.weather.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public void addLocation(LocationDto locationDto, User user) {
        if (locationRepository.existsByLatitudeAndLongitudeAndUser(locationDto.lat(), locationDto.lon(), user)) {
            throw new LocationAlreadyAddedException("The location " + locationDto.name() + " has already been added");
        }
        Location location = new Location();
        location.setName(locationDto.name());
        location.setLatitude(locationDto.lat());
        location.setLongitude(locationDto.lon());
        location.setUser(user);
        locationRepository.save(location);
    }

    @Transactional(readOnly = true)
    public List<Location> getUserLocations(User user) {
        if (user == null) {
            return new ArrayList<>();
        }
        return locationRepository.findByUser(user);
    }

    @Transactional
    public void deleteLocation(Long id, User user) {
        if (user == null) {
            throw new UserNotFoundException("Log in or register before you can add a location");
        }
        locationRepository.deleteByIdAndUser(id, user);
    }
}
