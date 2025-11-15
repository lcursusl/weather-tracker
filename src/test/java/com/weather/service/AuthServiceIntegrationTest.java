package com.weather.service;

import com.weather.config.TestConfig;
import com.weather.exception.UserAlreadyRegisteredException;
import com.weather.model.dto.RegistrationRequest;
import com.weather.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    void register_ShouldCreateUser() {
        RegistrationRequest request = new RegistrationRequest("test", "pass", "pass");

        User user = authService.register(request);

        assertNotNull(user.getId());
        assertEquals("test", user.getLogin());
    }

    @Test
    void register_DuplicateLogin_ShouldThrowException() {
        RegistrationRequest request = new RegistrationRequest("test", "pass", "pass");
        authService.register(request);

        assertThrows(UserAlreadyRegisteredException.class, () -> {
            authService.register(request);
        });
    }
}