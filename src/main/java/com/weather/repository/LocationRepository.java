package com.weather.repository;

import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class LocationRepository {
    private final SessionFactory sessionFactory;

    public LocationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Location location) {
        sessionFactory.getCurrentSession().persist(location);
    }

    @Transactional(readOnly = true)
    public List<Location> findByUser(User user) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Location as l where l.user = :user", Location.class)
                .setParameter("user", user)
                .list();
    }

    public void deleteByIdAndUser(Long id, User user) {
        sessionFactory.getCurrentSession()
                .createQuery("delete from Location as l where l.id = :id and l.user = :user")
                .setParameter("id", id)
                .setParameter("user", user)
                .executeUpdate();
    }

    public boolean existsByLatitudeAndLongitudeAndUser(BigDecimal latitude, BigDecimal longitude, User user) {
        Location location = sessionFactory.getCurrentSession()
                .createQuery("from Location as l where l.latitude = :latitude and l.longitude = :longitude and l.user = :user", Location.class)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("user", user)
                .uniqueResult();
        return location != null;
    }
}
