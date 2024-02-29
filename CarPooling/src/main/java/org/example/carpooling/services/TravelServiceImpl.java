package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {
    public static final String YOU_ARE_NOT_THE_CREATOR_OF_THE_TRAVEL_ERROR = "You are not the creator of the travel";
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
    public Travel update(User userModifier, Travel travelToUpdate) {
        //TODO double check the getByID since once you take the travelToUpdate here
        // then again is used getById in checkModifyPermission and one more time in the travelMapper.fromDTO
        Travel travel = getById(travelToUpdate.getTravelId());
        checkModifyPermission(userModifier, travelToUpdate);
        return travelRepository.update(travelToUpdate);
    }

    @Override
    public Travel delete(int id, User userModifier) {
        //TODO double check the getByID since once you take the travelToDelete here
        // then again is used getById in checkModifyPermission
        Travel travelToDelete = getById(id);
        checkModifyPermission(userModifier, travelToDelete);
        travelToDelete.setDeleted(true);
        return travelRepository.delete(travelToDelete);
    }

    @Override
    public long getTravelsCount() {
        return 0;
    }

    private void checkModifyPermission(User userModifier, Travel travelToUpdate) {

        if (userModifier.getUserId() != travelToUpdate.getUserId().getUserId()) {
            throw new AuthorizationException(YOU_ARE_NOT_THE_CREATOR_OF_THE_TRAVEL_ERROR);
        }
    }


}

