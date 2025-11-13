package com.weather.service;

import com.weather.model.entity.SessionEntity;
import com.weather.model.entity.User;
import com.weather.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public UUID createSession(User user) {
        UUID token = UUID.randomUUID();

        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(token);
        sessionEntity.setUser(user);
        sessionEntity.setExpiresAt(LocalDateTime.now().plusHours(1));
        sessionRepository.save(sessionEntity);

        return token;
    }

    @Transactional
    public void deleteSession(String token) {
        sessionRepository.deleteById(UUID.fromString(token));
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByToken(String token) {
        return sessionRepository.findUserById(UUID.fromString(token));
    }
}
