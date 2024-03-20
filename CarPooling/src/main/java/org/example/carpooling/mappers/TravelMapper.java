package org.example.carpooling.mappers;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TravelMapper {
    private final TravelService travelService;

    @Autowired
    public TravelMapper(TravelService travelService) {
        this.travelService = travelService;
    }

    public Travel fromDto(long id, TravelDto travelDto, User userModifier) {
        Travel travel = travelService.getById(id, userModifier);
        modifyTravel(travelDto, travel);
        return travel;
    }

    public Travel fromDto(TravelDto travelDto) {
        Travel travel = new Travel();
        modifyTravel(travelDto, travel);
        return travel;
    }

    private static void modifyTravel(TravelDto travelDto, Travel travel) {
        travel.setStartPoint(travelDto.getStartPoint());
        travel.setEndPoint(travelDto.getEndPoint());
        travel.setDepartureTime(travelDto.getDepartureTime());
        travel.setFreeSpots(travelDto.getFreeSpots());
        travel.setTravelStatus(travelDto.getTravelStatus());
        travel.setTravelComment(travelDto.getTravelComment());
    }

    public TravelDto convertToDto(Travel travel) {
        TravelDto travelDTO = new TravelDto();
        travelDTO.setTravelId(travel.getTravelId());
        travelDTO.setStartPoint(travel.getStartPoint());
        travelDTO.setEndPoint(travel.getEndPoint());
        travelDTO.setDepartureTime(travel.getDepartureTime());
        travelDTO.setFreeSpots(travel.getFreeSpots());
        travelDTO.setUserId(travel.getUserId());
        travelDTO.setCreator(travel.getUserId().getUsername());
        travelDTO.setTravelStatus(travel.getTravelStatus());
        travelDTO.setTravelComment(travel.getTravelComment());
        return travelDTO;
    }
}
