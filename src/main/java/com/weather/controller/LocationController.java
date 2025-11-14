package com.weather.controller;

import com.weather.exception.UserNotFoundException;
import com.weather.model.dto.LocationDto;
import com.weather.model.entity.User;
import com.weather.service.LocationService;
import com.weather.service.SessionService;
import com.weather.service.WeatherService;
import com.weather.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@Validated
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final SessionService sessionService;
    private final WeatherService weatherService;
    private final CookieUtil cookieUtil;

    public LocationController(LocationService locationService, SessionService sessionService, WeatherService weatherService, CookieUtil cookieUtil) {
        this.locationService = locationService;
        this.sessionService = sessionService;
        this.weatherService = weatherService;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping
    public String add(@ModelAttribute LocationDto locationDto,
                      HttpServletRequest request) {
        Optional<User> user = cookieUtil.getSessionId(request)
                .flatMap(sessionService::getUserByToken);

        user.ifPresent(u -> locationService.addLocation(locationDto, u));

        return "redirect:/home";
    }

    @PostMapping(("/{id}"))
    public String delete(@PathVariable Long id,
                         HttpServletRequest request) {
        User user = cookieUtil.getSessionId(request)
                .flatMap(sessionService::getUserByToken)
                .orElseThrow(() -> new UserNotFoundException("Log in or register before you can add a location"));

        locationService.deleteLocation(id, user);

        return "redirect:/home";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchQuery") @NotBlank String searchQuery,
                         Model model,
                         HttpServletRequest request) {
        Optional<User> user = cookieUtil.getSessionId(request)
                .flatMap(sessionService::getUserByToken);

        List<LocationDto> locations = weatherService.findLocationsByQuery(searchQuery);

        model.addAttribute("user", user.orElse(null));
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("foundLocations", locations);

        return "search-results";
    }
}
