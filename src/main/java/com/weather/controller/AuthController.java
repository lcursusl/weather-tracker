package com.weather.controller;

import com.weather.exception.UserAlreadyRegisteredException;
import com.weather.model.dto.RegistrationRequest;
import com.weather.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/sign-up")
    public String getSignUpPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", ""));
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String singUp(@Valid @ModelAttribute("registrationRequest") RegistrationRequest request,
                         BindingResult result,
                         HttpServletResponse response) {
        if (result.hasErrors()) {
            return "sign-up";
        }

        try {
            UUID token = authService.register(request);

            Cookie cookie = new Cookie("SESSION_ID", token.toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            return "redirect:/home";

        } catch (UserAlreadyRegisteredException e) {
            result.rejectValue("login", "login.error", e.getMessage());
            return "sign-up";
        }
    }
}
