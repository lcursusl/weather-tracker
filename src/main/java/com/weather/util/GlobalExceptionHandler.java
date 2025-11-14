package com.weather.util;

import com.weather.exception.LocationAlreadyAddedException;
import com.weather.exception.UserAlreadyRegisteredException;
import com.weather.exception.UserNotFoundException;
import com.weather.exception.WrongPasswordException;
import com.weather.model.dto.AuthenticationRequest;
import com.weather.model.dto.RegistrationRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public String handleUserAlreadyRegistered(UserAlreadyRegisteredException e, Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", ""));
        model.addAttribute("registrationError", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, Model model) {
        model.addAttribute("authenticationRequest", new AuthenticationRequest("", ""));
        model.addAttribute("authError", e.getMessage());
        return "sign-in";
    }

    @ExceptionHandler(WrongPasswordException.class)
    public String handleWrongPassword(WrongPasswordException e, Model model) {
        model.addAttribute("authenticationRequest", new AuthenticationRequest("", ""));
        model.addAttribute("authError", e.getMessage());
        return "sign-in";
    }

    @ExceptionHandler(LocationAlreadyAddedException.class)
    public String handleLocationAlreadyAdded(LocationAlreadyAddedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/home";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
