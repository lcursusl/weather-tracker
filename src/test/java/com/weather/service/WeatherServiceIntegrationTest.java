package com.weather.service;

import com.weather.config.TestConfig;
import com.weather.model.dto.LocationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherService weatherService;

    @BeforeEach
    void setUp() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);
        Field clientField = WeatherService.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(weatherService, mockHttpClient);

        ReflectionTestUtils.setField(weatherService, "apiKey", "test-key");
    }

    @Test
    void findLocationsByQuery_ShouldReturnLocations() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("""
            [
                {"name": "London", "lat": 51.5074, "lon": -0.1278, "country": "GB"}
            ]
            """);

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Field clientField = WeatherService.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(weatherService, mockHttpClient);

        List<LocationDto> locations = weatherService.findLocationsByQuery("London");

        assertFalse(locations.isEmpty());
        assertEquals("London", locations.get(0).name());
    }

    @Test
    void findLocationsByQuery_WithEmptyResponse_ShouldReturnEmptyList() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("[]");

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Field clientField = WeatherService.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(weatherService, mockHttpClient);

        List<LocationDto> locations = weatherService.findLocationsByQuery("UnknownCity");

        assertTrue(locations.isEmpty());
    }
}