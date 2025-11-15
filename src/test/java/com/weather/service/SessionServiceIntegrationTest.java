package com.weather.service;

import com.weather.config.TestConfig;
import com.weather.model.entity.User;
import com.weather.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createSession_ShouldWork() {
        User user = new User();
        user.setLogin("test");
        user.setPassword("pass");
        userRepository.save(user);

        UUID token = sessionService.createSession(user);

        assertNotNull(token);

        Optional<User> foundUser = sessionService.getUserByToken(token.toString());
        assertTrue(foundUser.isPresent());
        assertEquals("test", foundUser.get().getLogin());
    }
}