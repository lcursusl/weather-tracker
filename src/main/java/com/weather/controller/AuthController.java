package com.weather.controller;

import com.weather.exception.PasswordException;
import com.weather.exception.RepeatPasswordException;
import com.weather.exception.UsernameException;
import com.weather.model.dto.RegistrationRequest;
import com.weather.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String singUp(@ModelAttribute("registrationRequest") RegistrationRequest request,
                         HttpServletResponse response,
                         Model model) {
        try {
            UUID token = authService.register(request);

            Cookie cookie = new Cookie("SESSION_ID", token.toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);

            response.addCookie(cookie);

            return "redirect:/home";
        } catch (UsernameException e) {
            model.addAttribute("usernameError", e.getMessage());
            return "sign-up";
        } catch (PasswordException e) {
            model.addAttribute("passwordError", e.getMessage());
            return "sign-up";
        } catch (RepeatPasswordException e) {
            model.addAttribute("repeatPasswordError", e.getMessage());
            return "sign-up";
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "sign-up";
        }
    }
}
