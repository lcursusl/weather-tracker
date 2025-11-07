package com.weather.service;

import com.weather.model.entity.User;
import com.weather.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public UUID createSession(User user) {
        UUID token = UUID.randomUUID();
        sessionRepository.save(token, user);
        return token;
    }

    public void deleteSession(String token) {
        sessionRepository.deleteById(UUID.fromString(token));
    }

    public Optional<User> getUserByToken(String token) {
        return sessionRepository.findUserById(UUID.fromString(token));
    }
}
