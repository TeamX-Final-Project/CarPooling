package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Travel;

import java.util.List;

public interface TravelRepository {

    //ToDo implement filterOptions as parameter of the method
    List<Travel> getAllTravels();

    Travel getById(int id);

    Travel create();

    Travel updated();

    Travel delete();

    long getTravelsCount();
}
