package org.example.carpooling.services.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.models.enums.TravelStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TravelService {
    Page<Travel> getAllTravels(TravelFilterOptions travelFilterOptions);

    Travel getById(long id, User user);

    Travel create(Travel travel, User creator);

    Travel update(User userModifier, Travel travelToUpdate);

    Travel deleteTravelById(long id, User userModifier);

    Travel cancel(long id, User userModifier);

    int getCompletedTravelsAsDriverCount(User user);

    List<Travel> getOpenTravelsOfDriver(User user);

    List<Travel> getCompletedTravelsOfDriver(User user);

    long countTravelsByStatus(TravelStatus travelStatus);

    int getCompletedTravelsAsPassengerCount(User user);

    List<Travel> getMostRecentTravels();
}
