package org.example.carpooling.services;

import org.example.carpooling.Helpers;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.contracts.CandidateRepository;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.example.carpooling.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTests {

    @Mock
    CandidateRepository mockCandidateRepository;

    @Mock
    TravelRepository travelRepository;

    @InjectMocks
    TravelServiceImpl TravelService;

    @InjectMocks
    CandidateServiceImpl candidateService;

//    @Test
//    public void findById_Should_CallRepository(){
//        Optional<Candidates> candidates = createMockCandidateAccepted();
//
//        candidateService.findById(candidates.get().getId());
//
//        Mockito.verify(mockCandidateRepository, Mockito.times(1)).ifPresentOrElse();
//    }

    @Test
    public void findById_Should_Return_Candidate_WhenCandidateExists() {
        long candidateId = 1L;
        Candidates expectedCandidate = createMockCandidateAccepted();

        Mockito.when(mockCandidateRepository.findById(candidateId)).thenReturn(Optional.of(expectedCandidate));

        Candidates actualCandidate = candidateService.findById(candidateId);

        Assertions.assertEquals(expectedCandidate, actualCandidate);
    }

//    @Test
//    public void findById_Should_Throw_WhenCandidateDoesNotExist() {
//
//        long nonExistentCandidateId = 2L;
//        Candidates expectedCandidate = createMockCandidateAccepted();
//
//        Mockito.when(mockCandidateRepository.findById(nonExistentCandidateId)).thenReturn(Optional.of(expectedCandidate));
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> candidateService.findById(nonExistentCandidateId));
//    }
    @Test
    public void applyTravel_Should_Throw_Exception_When_User_Is_CreatorOfTravel(){
        Travel travel = createMockTravel();
        User user = createMockUserActive();
        travel.setUserId(user);

//        Mockito.when(travelRepository.findById())

        Assertions.assertThrows(AuthorizationException.class, () ->
                candidateService.applyTravel(travel.getTravelId(), user));
    }


}
