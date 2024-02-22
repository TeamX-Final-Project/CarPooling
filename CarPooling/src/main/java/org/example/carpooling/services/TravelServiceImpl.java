package org.example.carpooling.services;

import org.example.carpooling.models.Travel;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {
    private TravelRepository travelRepository;
    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
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
