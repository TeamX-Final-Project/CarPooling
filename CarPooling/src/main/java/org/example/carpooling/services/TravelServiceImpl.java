package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.helpers.TravelMapper;
import org.example.carpooling.models.*;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.models.enums.CandidateStatus;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.example.carpooling.services.contracts.TravelService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {
    public static final String YOU_ARE_NOT_THE_CREATOR_OF_THE_TRAVEL_ERROR = "You are not the creator of the travel";
    public static final String YOU_ARE_NOT_ALLOWED_TO_CREATE_A_TRAVEL_ERROR = "You are not allowed to create a travel";
    public static final String TRAVEL = "Travel";
    public static final String YOUR_STATUS_IS_NOT_ACTIVE_TO_REQUEST_THIS_INFORMATION_ERROR = "Your status is not active to request this information";

    private final TravelRepository travelRepository;

    @Autowired
    public TravelServiceImpl(UserService userService, TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    @Override
    public List<TravelDto> getAllTravels(TravelFilterOptions travelFilterOptions) {
        Pageable pageable = PageRequest.of(travelFilterOptions.getPage(),
                travelFilterOptions.getSize(),
                Sort.Direction.fromString(travelFilterOptions.getOrderBy()),
                travelFilterOptions.getSortBy());
        Specification<Travel> specification = Specification.where(TravelSpecifications.
                hasKeyword(travelFilterOptions.getKeyword(), travelFilterOptions.getSortBy()));
        Page<Travel> travelPage = travelRepository.findAll(specification, pageable);
        return travelPage.getContent().stream().map(this::convertToDto).toList();
    }

    private TravelDto convertToDto(Travel travel) {
        TravelDto travelDTO = new TravelDto();
        travelDTO.setStartPoint(travel.getStartPoint());
        travelDTO.setEndPoint(travel.getEndPoint());
        travelDTO.setDepartureTime(travel.getDepartureTime());
        travelDTO.setFreeSpots(travel.getFreeSpots());
        return travelDTO;
    }

    @Override
    public Travel getById(long id){//, User user) {
        //TODO create the logic for the authorization to search travel by ID but need to finish the MVC controller
        // for Authentication and User then uncomment this code
//        if (!UserStatus.ACTIVE.equals(user.getUserStatus())){
//            throw new AuthorizationException(YOUR_STATUS_IS_NOT_ACTIVE_TO_REQUEST_THIS_INFORMATION_ERROR);
//        }
        Travel travel = travelRepository.findById(id);
        if (travel == null){
            throw new EntityNotFoundException(TRAVEL, id);
        }
        return travel;
    }

    @Override
    public Travel create(Travel travel, User creator) {
        travel.setUserId(creator);
        checkIsUserActive(creator);
        calculateTravelInformation(travel);
        return travelRepository.save(travel);
    }


    @Override
    public Travel update(User userModifier, Travel travelToUpdate) {
        calculateTravelInformation(travelToUpdate);
        checkModifyPermission(userModifier, travelToUpdate);
        return travelRepository.save(travelToUpdate);
    }

    @Override
    public Travel deleteTravelById(int id, User userModifier) {
        Travel travelToDelete = getById(id);
        checkModifyPermission(userModifier, travelToDelete);
        travelToDelete.setDeleted(true);
        return travelRepository.save(travelToDelete);
    }

    @Override
    public Travel cancel(int id, User userModifier) {
        Travel travelToCancel = getById(id);
        checkModifyPermission(userModifier, travelToCancel);
        travelToCancel.setTravelStatus(TravelStatus.CANCELED);
        return travelRepository.save(travelToCancel);
    }

    @Override
    public long getTravelsCount() {
        return 0;
    }

    private void checkModifyPermission(User userModifier, Travel travel) {

        if (userModifier.getUserId() != travel.getUserId().getUserId()) {
            throw new AuthorizationException(YOU_ARE_NOT_THE_CREATOR_OF_THE_TRAVEL_ERROR);
        }
    }

    private static void calculateTravelInformation(Travel travel) {
        String startPoint = travel.getStartPoint();
        String endPoint = travel.getEndPoint();
        int[] distanceTravel = DistanceTravelImpl.getTravelDetails(startPoint, endPoint);
        int[] durationTravel = DistanceTravelImpl.getTravelDetails(startPoint, endPoint);
        travel.setDistanceTravel(distanceTravel[0]);
        travel.setDurationTravel(durationTravel[1]);
    }

    private static void checkIsUserActive(User creator) {
        if (!UserStatus.ACTIVE.equals(creator.getUserStatus())) {
            throw new OperationNotAllowedException(YOU_ARE_NOT_ALLOWED_TO_CREATE_A_TRAVEL_ERROR);
        }
    }
}

