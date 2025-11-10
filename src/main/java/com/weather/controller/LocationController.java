package com.weather.controller;

import com.weather.exception.UserNotFoundException;
import com.weather.model.dto.LocationDto;
import com.weather.model.entity.User;
import com.weather.service.LocationService;
import com.weather.service.SessionService;
import com.weather.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final WeatherService weatherService;

    public LocationController(LocationService locationService, SessionService sessionService, WeatherService weatherService) {
        this.locationService = locationService;
        this.sessionService = sessionService;
        this.weatherService = weatherService;
    }

    @PostMapping
    public String add(@ModelAttribute LocationDto locationDto,
                      BindingResult result,
                      HttpServletRequest request) {
        try {
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
            locationService.addLocation(locationDto, user);

            return "redirect:/home";
        } catch (UserNotFoundException e){
            result.rejectValue("error", "add.error", e.getMessage());
            return "redirect:/home";
        }
    }

    @PostMapping(("/{id}"))
    public String delete(@PathVariable Long id, Model model) {

    }

    @GetMapping("/search")
    public String search(@RequestParam @NotBlank String searchQuery,
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
        List<LocationDto> locations = weatherService.findLocationsByQuery(searchQuery);

        model.addAttribute("user", user.orElse(null));
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("foundLocations", locations);
        return "search-results";
    }
}
