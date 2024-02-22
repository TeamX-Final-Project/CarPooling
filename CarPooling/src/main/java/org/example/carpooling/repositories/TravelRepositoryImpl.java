package org.example.carpooling.repositories;

import org.example.carpooling.models.Travel;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TravelRepositoryImpl implements TravelRepository {

    private final SessionFactory sessionFactory;

    public TravelRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Travel> getAllTravels() {
        return null;
    }

    @Override
    public Travel getById(int id) {
        return null;
    }

    @Override
    public Travel create() {
        return null;
    }

    @Override
    public Travel updated() {
        return null;
    }

    @Override
    public Travel delete() {
        return null;
    }

    @Override
    public long getTravelsCount() {
        return 0;
    }
}
