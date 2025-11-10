package com.weather.repository;

import com.weather.model.dto.LocationDto;
import com.weather.model.entity.Location;
import com.weather.model.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class LocationRepository {
    private final SessionFactory sessionFactory;

    public LocationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(LocationDto locationDto, User user) {
        Location location = new Location();
        location.setName(locationDto.name());
        location.setLatitude(locationDto.lat());
        location.setLongitude(locationDto.lon());
        location.setUser(user);
        sessionFactory.getCurrentSession().persist(location);
    }

    @Transactional(readOnly = true)
    public List<Location> findByUser(User user) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Location as l where l.user = :user", Location.class)
                .setParameter("user", user)
                .list();
    }
}
