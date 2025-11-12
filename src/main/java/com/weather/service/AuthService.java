package com.weather.service;

import com.weather.exception.UserAlreadyRegisteredException;
import com.weather.exception.UserNotFoundException;
import com.weather.exception.WrongPasswordException;
import com.weather.model.dto.AuthenticationRequest;
import com.weather.model.dto.RegistrationRequest;
import com.weather.model.entity.User;
import com.weather.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(RegistrationRequest request) {
        User user = new User();
        user.setLogin(request.login());
        user.setPassword(BCrypt.hashpw(request.password(), BCrypt.gensalt()));

        if (userRepository.findByLogin(request.login()).isPresent()) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        userRepository.save(user);
        return user;
    }

    @Transactional
    public User authenticate(AuthenticationRequest request) {
        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!BCrypt.checkpw(request.password(), user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
        return user;
    }
}
