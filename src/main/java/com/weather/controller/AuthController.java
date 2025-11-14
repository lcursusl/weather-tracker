package com.weather.controller;

import com.weather.model.dto.AuthenticationRequest;
import com.weather.model.dto.RegistrationRequest;
import com.weather.model.entity.User;
import com.weather.service.AuthService;
import com.weather.service.SessionService;
import com.weather.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final SessionService sessionService;
    private final CookieUtil cookieUtil;

    public AuthController(AuthService authService, SessionService sessionService, CookieUtil cookieUtil) {
        this.authService = authService;
        this.sessionService = sessionService;
        this.cookieUtil = cookieUtil;
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
        if (!request.password().equals(request.repeatPassword())) {
            result.rejectValue("repeatPassword", "repeatPassword.error", "Passwords do not match");
            return "sign-up";
        }
        User user = authService.register(request);
        UUID token = sessionService.createSession(user);

        Cookie cookie = cookieUtil.createCookie(token);
        response.addCookie(cookie);

        return "redirect:/home";
    }

    @GetMapping("/sign-in")
    public String getSignInPage(Model model) {
        model.addAttribute("authenticationRequest", new AuthenticationRequest("", ""));
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@Valid @ModelAttribute("authenticationRequest") AuthenticationRequest request,
                         BindingResult result,
                         HttpServletResponse response) {
        if (result.hasErrors()) {
            return "sign-in";
        }
        User user = authService.authenticate(request);
        UUID token = sessionService.createSession(user);

        Cookie cookie = cookieUtil.createCookie(token);
        response.addCookie(cookie);

        return "redirect:/home";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request,
                          HttpServletResponse response) {
        cookieUtil.getSessionId(request)
                .ifPresent(sessionService::deleteSession);

        Cookie cookie = cookieUtil.invalidateCookie();
        response.addCookie(cookie);

        return "redirect:/home";
    }
}
