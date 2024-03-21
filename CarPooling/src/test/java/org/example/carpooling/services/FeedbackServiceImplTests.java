package org.example.carpooling.services;


import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.TravelException;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.contracts.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
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
    FeedbackRepository feedbackRepository;


    @Test
    void testGetFeedbackById_ExistingFeedback() {
        Long feedbackId = 1L;
        Feedback expectedFeedback = new Feedback();
        expectedFeedback.setId(feedbackId);

        when(feedbackRepository.findById(feedbackId)).thenReturn(java.util.Optional.of(expectedFeedback));

        Feedback resultFeedback = feedbackService.getById(feedbackId);

        assertEquals(expectedFeedback, resultFeedback);
    }

    @Test
    void testGetFeedbackById_NonExistingFeedback() {
        Long nonExcitingFeedback = 1L;

        when(feedbackRepository.findById(nonExcitingFeedback)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> feedbackService.getById(nonExcitingFeedback));
    }


    @Test
    void testCreateValidFeedback() {
        Feedback validFeedback = createValidFeedback();

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(validFeedback);

        // Act
        feedbackService.create(validFeedback);

        // Assert
        verify(feedbackRepository, times(1)).save(validFeedback);
    }

    @Test
    void testCreateFeedbackForIncompleteTravel() {
        // Arrange
        Feedback invalidFeedback = createValidFeedback();
        Travel travel = new Travel();

        when(feedbackRepository.save(invalidFeedback));

        assertThrows(TravelException.class, () -> feedbackService.create(invalidFeedback));
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
        Travel travel = new Travel();
        User driver = new User();
        travel.setUserId(driver);
        duplicateFeedback.setTravel(travel);
        doThrow(new EntityDuplicateException(FEEDBACK_ALREADY_GIVEN_ERROR))
                .when(feedbackRepository)
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

        when(feedbackRepository.findAllByReceiver(receiverUser)).thenReturn(feedbacks);

        List<Feedback> resultFeedbacks = feedbackService.getByReceiver(receiverUser);
        assertEquals(2, resultFeedbacks.size());

        verify(feedbackRepository, times(1)).findAllByReceiver(receiverUser);
    }

    private Feedback createValidFeedback() {
        User giver = new User();
        User receiver = new User();
        Travel travel = new Travel();
        return new Feedback();
    }


}
