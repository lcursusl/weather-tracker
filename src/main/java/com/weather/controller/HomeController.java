package com.weather.controller;

import com.weather.model.entity.User;
import com.weather.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final SessionService sessionService;

    public HomeController(SessionService sessionService) {
        this.sessionService = sessionService;
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
        model.addAttribute("user", user.orElse(null));
        return "home";
    }
}
