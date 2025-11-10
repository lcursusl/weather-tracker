package com.weather.controller;

import com.weather.model.dto.WeatherDto;
import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import com.weather.service.LocationService;
import com.weather.service.SessionService;
import com.weather.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final SessionService sessionService;
    private final LocationService locationService;
    private final WeatherService weatherService;

    public HomeController(SessionService sessionService, LocationService locationService, WeatherService weatherService) {
        this.sessionService = sessionService;
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public String getHomePage(Model model,
                              HttpServletRequest request) {
        Optional<User> user = Optional.empty();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SESSION_ID")) {
                    user = sessionService.getUserByToken(cookie.getValue());
                    break;
                }
            }
        }

        List<Location> locations = locationService.getUserLocations(user);
        List<WeatherDto> weathers = weatherService.getWeather(locations);

        model.addAttribute("user", user);
        model.addAttribute("weathers", weathers);
        return "home";
    }
}
