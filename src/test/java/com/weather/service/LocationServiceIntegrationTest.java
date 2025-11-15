package com.weather.service;

import com.weather.config.TestConfig;
import com.weather.model.dto.LocationDto;
import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import com.weather.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class LocationServiceIntegrationTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void addLocation_ShouldSaveToDatabase() {
        User user = createUser();
        LocationDto locationDto = new LocationDto("London", new BigDecimal("51.5074"), new BigDecimal("-0.1278"), "", "");

        locationService.addLocation(locationDto, user);

        List<Location> locations = locationService.getUserLocations(user);
        assertEquals(1, locations.size());
        assertEquals("London", locations.get(0).getName());
    }

    private User createUser() {
        User user = new User();
        user.setLogin("test");
        user.setPassword("pass");
        userRepository.save(user);
        return user;
    }
}