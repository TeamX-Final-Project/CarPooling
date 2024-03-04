package org.example.carpooling.helpers;

import org.example.carpooling.models.Travel;
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

    public Travel fromDto(int id, TravelDto travelDto) {
        Travel travel = travelService.getById(id);
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
}
