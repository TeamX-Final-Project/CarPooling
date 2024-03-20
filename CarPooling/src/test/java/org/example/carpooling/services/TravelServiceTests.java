package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.example.carpooling.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class TravelServiceTests {

    @Mock
    TravelRepository mockTravelRepository;

    @InjectMocks
    TravelServiceImpl TravelService;

//    @Test
//    public void getAllTravels_Should_CallRepository(){
//        //Arrange
//        TravelFilterOptions mockTravelFilterOptions = createTravelFilterOptions();
//
//        Mockito.when(mockTravelRepository.findAll(mockTravelFilterOptions.getKeyword(), mockTravelFilterOptions.getPage())).thenReturn(null);
//
//        //Act,Assert
//        mockTravelService.getAllTravels(mockTravelFilterOptions);
//        Mockito.verify(mockTravelRepository, Mockito.times(1)).findAll((Pageable) mockTravelFilterOptions);
//    }

    @Test
    public void getById_Should_Throw_Exception_When_UserStatusIsNotActive() {
        //Arrange
        User user = createMockUser();
        long travelId = 2L;

        //Act, Assert


        Assertions.assertThrows(AuthorizationException.class, () ->
                TravelService.getById(travelId, user));
    }

    @Test
    public void getById_Should_Throw_Exception_When_TravelDoesNotExist() {
        //Arrange
        User user = createMockUserActive();
        Travel travel = createMockTravel();
        long travelId = 2L;


        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                TravelService.getById(travelId, user));
    }

    @Test
    public void getById_Should_Return_Travel() {
        //Arrange
        User user = createMockUserActive();
        Travel travel = createMockTravel();

        Mockito.when(mockTravelRepository.findById(travel.getTravelId())).thenReturn(travel);

        //Act
        Travel result = TravelService.getById(travel.getTravelId(), user);
        // Assert
        Assertions.assertEquals(result, travel);
    }

    @Test
    public void create_Should_Throw_Exception_When_UserIsNotActive() {
        //Arrange
        User user = createMockUser();
        Travel travel = createMockTravel();

        //Act, Assert
        Assertions.assertThrows(OperationNotAllowedException.class, () ->
                TravelService.create(travel, user));
    }

    @Test
    public void create_Should_Save_New_Travel() {
        //Arrange
        User user = createMockUserActive();

        Travel travel = Mockito.mock(Travel.class);
        Mockito.when(travel.getStartPoint()).thenReturn("Sofia");
        Mockito.when(travel.getEndPoint()).thenReturn("Plovdiv");

        Mockito.when(mockTravelRepository.save(travel)).thenReturn(travel);

        //Act
        TravelService.create(travel, user);
        // Assert
        Mockito.verify(mockTravelRepository).save(travel);
    }

    @Test
    public void update_Should_Throw_Authorization_Exception_IfUserIsNotAdmin_OrCreatorOfTravel(){
        //Arrange
        Travel travel = createMockTravel();
        User user = createMockUserActive();
        User notCreator = createMockUserActive();
        travel.setUserId(user);
        notCreator.setUserId(5L);

        //Act, Assert
        Assertions.assertThrows(AuthorizationException.class, () ->
                TravelService.update(notCreator, travel));
    }

    @Test
    public void update_Should_Save_Updated_Travel(){
        //Arrange
        User user = createMockUserActive();
//        Travel travel = createMockTravel();
        Travel updatedTravel = createMockTravel();
        updatedTravel.setStartPoint("Plovdiv");
        updatedTravel.setEndPoint("Sofia");
        updatedTravel.setDepartureTime(LocalDateTime.parse("2023-08-30T18:00"));
        updatedTravel.setFreeSpots(3);
        updatedTravel.setUserId(user);
        updatedTravel.setDistanceTravel(223);
        updatedTravel.setDurationTravel(224);
        updatedTravel.setTravelComment("MockTravelComments");

        //Act
        TravelService.update(user,updatedTravel);

        //Assert
        Mockito.verify(mockTravelRepository,Mockito.times(1))
                .save(updatedTravel);
    }
    @Test
    public void delete_Should_Save_Deleted_Travel(){
        //Arrange
        User user = createMockUserActive();
        Travel travel = createMockTravel();

        Travel deletedTravel = createMockTravel();
        deletedTravel.setDeleted(true);

        Mockito.when(mockTravelRepository.findById(travel.getTravelId())).thenReturn(travel);

        //Act
        TravelService.deleteTravelById(travel.getTravelId(), user);
        //Assert
        Mockito.verify(mockTravelRepository, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    public void delete_Should_Throw_Authorization_Exception_IfUserIsNotAdmin_OrCreatorOfTravel(){
        //Arrange
        User user = createMockUserActive();
        Travel travel = createMockTravel();
        User notCreator = createMockUserActive();
        travel.setUserId(user);
        notCreator.setUserId(5L);

        Mockito.when(mockTravelRepository.findById(travel.getTravelId()))
                .thenReturn(travel);


        //Act, Assert
        Assertions.assertThrows(AuthorizationException.class, () ->
                TravelService.deleteTravelById(travel.getTravelId(), notCreator));
    }

    @Test
    public void cancel_Should_Save_Canceled_Travel(){
        //Arrange
        User user = createMockUserActive();
        Travel travel = createMockTravel();

        Travel canceledTravel = createMockTravel();
        canceledTravel.setTravelStatus(TravelStatus.CANCELED);

        Mockito.when(mockTravelRepository.findById(travel.getTravelId())).thenReturn(travel);

        //Act
        TravelService.cancel(travel.getTravelId(), user);
        //Assert
        Mockito.verify(mockTravelRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void cancel_Should_Throw_Authorization_Exception_IfUserIsNotAdmin_OrCreatorOfTravel(){
        //Arrange
        User user = createMockUserActive();
        Travel travel = createMockTravel();
        User notCreator = createMockUserActive();
        travel.setUserId(user);
        notCreator.setUserId(5L);

        Mockito.when(mockTravelRepository.findById(travel.getTravelId()))
                .thenReturn(travel);


        //Act, Assert
        Assertions.assertThrows(AuthorizationException.class, () ->
                TravelService.cancel(travel.getTravelId(), notCreator));
    }



}
