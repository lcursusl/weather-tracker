package com.weather.controller;

import com.weather.model.dto.WeatherDto;
import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import com.weather.service.LocationService;
import com.weather.service.SessionService;
import com.weather.service.WeatherService;
import com.weather.util.CookieUtil;
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
    private final CookieUtil cookieUtil;

    public HomeController(SessionService sessionService, LocationService locationService, WeatherService weatherService, CookieUtil cookieUtil) {
        this.sessionService = sessionService;
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.cookieUtil = cookieUtil;
    }

    @GetMapping
    public String getHomePage(Model model,
                              HttpServletRequest request) {
        Optional<String> sessionId = cookieUtil.getSessionId(request);
        Optional<User> user = sessionService.getUserByToken(sessionId.orElse(""));
        List<Location> locations = locationService.getUserLocations(user.orElse(null));
        List<WeatherDto> weathers = weatherService.getWeather(locations);

        model.addAttribute("user", user.orElse(null));
        model.addAttribute("weathers", weathers);

        return "home";
    }
}
