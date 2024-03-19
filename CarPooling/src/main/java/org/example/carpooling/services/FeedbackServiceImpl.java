package org.example.carpooling.services;

import org.example.carpooling.helpers.AuthorizationHelper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.contracts.FeedbackRepository;
import org.example.carpooling.services.contracts.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    public static final String DELETE_ONLY_GIVEN_FEEDBACKS_ERROR = "You should be an author of the feedback";
    public static final String TRAVEL_NOT_COMPLETED_YET_ERROR = "Travel hasn't been completed yet";
    public static final String GIVER_IS_NOT_TRAVEL_DRIVER_ERROR = "You should be the driver of the travel";
    public static final String GIVER_IS_NOT_TRAVEL_PASSENGER_ERROR = "You should be a passenger in the travel";
    public static final String RECEIVER_IS_NOT_TRAVEL_PASSENGER_ERROR = "User is not passenger in the travel";
    public static final String RECEIVER_IS_NOT_TRAVEL_DRIVER_ERROR = "User is not the driver of the travel";
    private final FeedbackRepository feedbackRepository;
    private final AuthorizationHelper authorizationHelper;
    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, AuthorizationHelper authorizationHelper) {
        this.feedbackRepository = feedbackRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public Feedback getById(Long id) {
        return feedbackRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Feedback> getByReceiver(User user) {
        return feedbackRepository.findAllByReceiver(user);
    }

    @Override
    public void create(Feedback feedback){
        authorizationHelper.checkIfTravelPassed(feedback.getTravel().getUserId().getUserId(),
                TRAVEL_NOT_COMPLETED_YET_ERROR);
        if(feedback.getTravel().getUserId().equals(feedback.getGiver())){
            authorizationHelper.checkIfIsDriver(
                    feedback.getGiver(),
                    feedback.getTravel(),
                    GIVER_IS_NOT_TRAVEL_DRIVER_ERROR);
            authorizationHelper.checkIfIsPassenger(
                    feedback.getReceiver(),
                    feedback.getTravel(),
                    RECEIVER_IS_NOT_TRAVEL_PASSENGER_ERROR);
        }
        else {
            authorizationHelper.checkIfIsPassenger(feedback.getGiver(),
                    feedback.getTravel(),
                    GIVER_IS_NOT_TRAVEL_PASSENGER_ERROR);
            authorizationHelper.checkIfIsDriver(feedback.getReceiver(),
                    feedback.getTravel(),
                    RECEIVER_IS_NOT_TRAVEL_DRIVER_ERROR);
        }
        authorizationHelper.checkDuplicateFeedback(feedback);
        feedbackRepository.save(feedback);
    }

    @Override
    public void delete(User modifier, Feedback feedback) {
        authorizationHelper.checkSelfModifyPermissions(
                modifier,
                feedback.getGiver(),
                DELETE_ONLY_GIVEN_FEEDBACKS_ERROR);
    }

    @Override
    public Double getAverageRatingForUser(User user) {
        return feedbackRepository.getAverageRatingForReceiver(user);
    }
}
