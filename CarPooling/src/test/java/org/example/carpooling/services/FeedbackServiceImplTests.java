package org.example.carpooling.services;


import org.example.carpooling.Helpers;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.exceptions.TravelException;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.repositories.contracts.CandidateRepository;
import org.example.carpooling.repositories.contracts.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.example.carpooling.services.FeedbackServiceImpl.FEEDBACK_ALREADY_GIVEN_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTests {

    @InjectMocks
    FeedbackServiceImpl feedbackService;

    @Mock
    FeedbackRepository mockFeedbackRepository;
    @Mock
    CandidateRepository mockCandidateRepository;


    @Test
    void testGetFeedbackById_ExistingFeedback() {
        Long feedbackId = 1L;
        Feedback expectedFeedback = new Feedback();
        expectedFeedback.setId(feedbackId);

        when(mockFeedbackRepository.findById(feedbackId)).thenReturn(java.util.Optional.of(expectedFeedback));

        Feedback resultFeedback = feedbackService.getById(feedbackId);

        assertEquals(expectedFeedback, resultFeedback);
    }

    @Test
    void testGetFeedbackById_NonExistingFeedback() {
        Long nonExcitingFeedback = 1L;

        when(mockFeedbackRepository.findById(nonExcitingFeedback)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> feedbackService.getById(nonExcitingFeedback));
    }


    @Test
    void testCreateValidFeedback() {
        Feedback validFeedback = createValidFeedback();
        Travel travel = Helpers.createMockTravel();

        when(mockFeedbackRepository.save(any(Feedback.class))).thenReturn(validFeedback);

        // Act
        feedbackService.create(validFeedback);

        // Assert
        verify(mockFeedbackRepository, times(1)).save(validFeedback);
    }

    @Test
    void testCreateFeedbackForIncompleteTravel() {
        // Arrange
        Feedback invalidFeedback = createValidFeedback();
        LocalDateTime departureTime = LocalDateTime.now().plusDays(1);
        invalidFeedback.getTravel().setDepartureTime(departureTime);

        assertThrows(OperationNotAllowedException.class, () -> feedbackService.create(invalidFeedback));
    }

    @Test
    void testCreateFeedbackWithInvalidGiverAndReceiver() {
        Feedback invalidFeedback = new Feedback();
        Travel mockTravel = new Travel();
        User mockGiver = new User();
        User mockReceiver = new User();
        invalidFeedback.setTravel(mockTravel);
        invalidFeedback.setGiver(mockGiver);
        invalidFeedback.setReceiver(mockReceiver);

        assertThrows(RuntimeException.class, () -> feedbackService.create(invalidFeedback));
    }

    @Test
    void testCreateFeedbackWithDuplicateFeedback() {
        Feedback duplicateFeedback = createValidFeedback();
        Travel travel = Helpers.createMockTravel();
        User driver = new User();
        travel.setUserId(driver);
        duplicateFeedback.setTravel(travel);
        duplicateFeedback.setGiver(driver);

        doThrow(new EntityDuplicateException(FEEDBACK_ALREADY_GIVEN_ERROR))
                .when(mockFeedbackRepository)
                .findByGiverAndReceiverAndTravel(duplicateFeedback.getGiver(), duplicateFeedback.getReceiver(), travel);

        assertThrows(EntityDuplicateException.class, () -> feedbackService.create(duplicateFeedback));
    }

    @Test
    void testGetByReceiver() {
        User receiverUser = new User();
        receiverUser.setUserId(1L);
        Feedback feedback1 = new Feedback();
        Feedback feedback2 = new Feedback();
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(feedback1);
        feedbacks.add(feedback2);

        when(mockFeedbackRepository.findAllByReceiver(receiverUser)).thenReturn(feedbacks);

        List<Feedback> resultFeedbacks = feedbackService.getByReceiver(receiverUser);
        assertEquals(2, resultFeedbacks.size());

        verify(mockFeedbackRepository, times(1)).findAllByReceiver(receiverUser);
    }

    private Feedback createValidFeedback() {
        Feedback feedback = new Feedback();
        feedback.setGiver(Helpers.createMockUser());
        feedback.setReceiver(Helpers.createMockUser());
        feedback.setTravel(Helpers.createMockTravel());
        return feedback;
    }


}
