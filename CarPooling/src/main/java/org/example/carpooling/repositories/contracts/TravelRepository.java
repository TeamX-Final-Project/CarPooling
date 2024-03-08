package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;

import java.util.List;

public interface TravelRepository {

    //ToDo implement filterOptions as parameter of the method
    List<Travel> getAllTravels(TravelFilterOptions travelFilterOptions);

    Travel getById(int id);

    Travel create(Travel travel);

    Travel update(Travel travelToUpdate);

    Travel delete(Travel travelToDelete);

    Travel cancel(Travel travelToCancel);

    long getTravelsCount();
}
