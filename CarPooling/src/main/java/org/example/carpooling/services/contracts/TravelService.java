package org.example.carpooling.services.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TravelService {
    List<TravelDto> getAllTravels(TravelFilterOptions travelFilterOptions);

    Travel getById(int id);

    Travel create(Travel travel, User creator);

    Travel update(User userModifier, Travel travelToUpdate);

    Travel deleteTravelById(int id, User userModifier);

    Travel cancel(int id, User userModifier);

    Candidates applyTravel(int id, User userToApply);

    long getTravelsCount();
}
