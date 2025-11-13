package com.weather.util;

import com.weather.exception.LocationAlreadyAddedException;
import com.weather.exception.UserAlreadyRegisteredException;
import com.weather.exception.UserNotFoundException;
import com.weather.exception.WrongPasswordException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public String handleUserAlreadyRegistered(UserAlreadyRegisteredException e, BindingResult result) {
        result.rejectValue("login", "login.error", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, BindingResult result) {
        result.rejectValue("login", "login.error", e.getMessage());
        return "sign-in";
    }

    @ExceptionHandler(WrongPasswordException.class)
    public String handleWrongPassword(WrongPasswordException e, BindingResult result) {
        result.rejectValue("password", "password.error", e.getMessage());
        return "sign-in";
    }

    @ExceptionHandler(LocationAlreadyAddedException.class)
    public String handleLocationAlreadyAdded(LocationAlreadyAddedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/home";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException() {
        return "error";
    }
}
