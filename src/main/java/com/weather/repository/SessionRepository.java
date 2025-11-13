package com.weather.repository;

import com.weather.model.entity.SessionEntity;
import com.weather.model.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepository {
    private final SessionFactory sessionFactory;

    public SessionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(SessionEntity sessionEntity) {
        sessionFactory.getCurrentSession().persist(sessionEntity);
    }

    public void deleteById(UUID id) {
        SessionEntity sessionEntity = sessionFactory.getCurrentSession().get(SessionEntity.class, id);
        if (sessionEntity != null) {
            sessionFactory.getCurrentSession().remove(sessionEntity);
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserById(UUID id) {
        return sessionFactory.getCurrentSession()
                .createQuery("select s.user from SessionEntity as s where s.id = :id", User.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }
}
