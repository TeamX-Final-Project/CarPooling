package org.example.carpooling.mappers;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.SimpleTravelDto;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.models.enums.CandidateStatus;
import org.example.carpooling.services.contracts.CandidateService;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TravelMapper {
    private final TravelService travelService;
    private final CandidateService candidateService;

    @Autowired
    public TravelMapper(TravelService travelService, CandidateService candidateService) {
        this.travelService = travelService;
        this.candidateService = candidateService;
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

    public SimpleTravelDto convertToSimpleTravelDto(User user, Travel travel) {
        SimpleTravelDto simpleTravelDTO = new SimpleTravelDto();
        CandidateStatus status = null;
        Optional<Candidates> optionalCandidate = candidateService.checkAppliedUsers(user, travel);
        if (optionalCandidate.isPresent()) {
            status = optionalCandidate.get().getStatus();
        }
        simpleTravelDTO.setCurrentUserStatus(status);
        simpleTravelDTO.setTravelId(travel.getTravelId());
        simpleTravelDTO.setUserId(travel.getUserId());
        simpleTravelDTO.setStartPoint(travel.getStartPoint());
        simpleTravelDTO.setEndPoint(travel.getEndPoint());
        simpleTravelDTO.setDepartureTime(travel.getDepartureTime());
        simpleTravelDTO.setFreeSpots(travel.getFreeSpots());

        return simpleTravelDTO;
    }
}
