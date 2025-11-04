package com.weather.service;

import com.weather.exception.UserAlreadyRegisteredException;
import com.weather.model.dto.RegistrationRequest;
import com.weather.model.entity.User;
import com.weather.repository.SessionRepository;
import com.weather.repository.UserRepository;
import com.weather.util.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public UUID register(RegistrationRequest request) {
        Validator.validateUsername(request.login());
        Validator.validatePassword(request.password(), request.repeatPassword());

        User user = new User();
        user.setLogin(request.login());
        user.setPassword(BCrypt.hashpw(request.password(), BCrypt.gensalt()));

        if (userRepository.findByLogin(request.login()).isPresent()) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        userRepository.save(user);
        UUID token = UUID.randomUUID();
        sessionRepository.save(token, user);

        return token;
    }
}
