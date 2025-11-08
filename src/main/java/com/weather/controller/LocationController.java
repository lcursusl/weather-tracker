package com.weather.controller;

import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import com.weather.service.LocationService;
import com.weather.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Validated
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final SessionService sessionService;

    public LocationController(LocationService locationService, SessionService sessionService) {
        this.locationService = locationService;
        this.sessionService = sessionService;
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("location") Location location,
                      Model model) {
        model.addAttribute("location", location);
        return "location";
    }

    @PostMapping(("/{id}"))
    public String delete(@PathVariable Long id, Model model) {

    }

    @GetMapping("/search")
    public String search(@RequestParam @NotBlank String location,
                         Model model,
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
        model.addAttribute("user", user.orElse(null));

        List<LocationDto> locations = locationService.findLocations(location);

        model.addAttribute("searchQuery", location);
        model.addAttribute("foundLocations", locations);
        return "search-results";
    }
}

//Поиск локаций по названию - https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
//Получение погоды по координатам локации - https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
