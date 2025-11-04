package com.weather.repository;

import com.weather.model.entity.SessionEntity;
import com.weather.model.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class SessionRepository {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(UUID token, User user) {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(token);
        sessionEntity.setUser(user);
        sessionEntity.setExpiresAt(LocalDateTime.now().plusHours(1));

        sessionFactory.getCurrentSession().persist(sessionEntity);
    }
}
