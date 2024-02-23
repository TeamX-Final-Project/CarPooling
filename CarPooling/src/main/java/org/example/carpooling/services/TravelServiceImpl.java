package org.example.carpooling.services;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;

    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    @Override
    public List<Travel> getAllTravels(TravelFilterOptions travelFilterOptions) {
        return travelRepository.getAllTravels(travelFilterOptions);
    }

    @Override
    public Travel getById(int id) {
        //TODO create the logic for the authorization to search travel by ID
        return travelRepository.getById(id);
    }

    @Override
    public Travel create(Travel travel, User creator) {
        //        TODO create the logic for the authorization and check if the user is blocked before creating new travel

        return travelRepository.create(travel);
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
