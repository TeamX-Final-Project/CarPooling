package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.TravelException;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;
import org.example.carpooling.repositories.contracts.CandidateRepository;
import org.example.carpooling.repositories.contracts.FeedbackRepository;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.example.carpooling.services.contracts.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private static final String DELETE_ONLY_GIVEN_FEEDBACKS_ERROR = "You should be an author of the feedback";
    private static final String TRAVEL_NOT_COMPLETED_YET_ERROR = "Travel hasn't been completed yet";
    private static final String GIVER_IS_NOT_TRAVEL_DRIVER_ERROR = "You should be the driver of the travel";
    private static final String GIVER_IS_NOT_TRAVEL_PASSENGER_ERROR = "You should be a passenger in the travel";
    private static final String RECEIVER_IS_NOT_TRAVEL_PASSENGER_ERROR = "User is not passenger in the travel";
    private static final String RECEIVER_IS_NOT_TRAVEL_DRIVER_ERROR = "User is not the driver of the travel";
    private static final String FEEDBACK_ALREADY_GIVEN_ERROR = "You already gave your feedback";

    private final FeedbackRepository feedbackRepository;
    private final CandidateRepository candidateRepository;
    private final TravelRepository travelRepository;
    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               CandidateRepository candidateRepository, TravelRepository travelRepository) {
        this.feedbackRepository = feedbackRepository;

        this.candidateRepository = candidateRepository;
        this.travelRepository = travelRepository;
    }

    @Override
    public Feedback getById(Long id) {
        return feedbackRepository.findById(id).orElseThrow();
    }

    public List<Feedback> getByReceiver(User user) {
        return feedbackRepository.findAllByReceiver(user);
    }

    @Override
    public void create(Feedback feedback){
        checkIfTravelPassed(feedback.getTravel().getUserId().getUserId());
        if(feedback.getTravel().getUserId().equals(feedback.getGiver())){
            checkIfIsDriver(feedback.getGiver(), feedback.getTravel());
            checkIfIsPassenger(feedback.getReceiver(), feedback.getTravel());
        }
        checkDuplicateFeedback(feedback);
        feedbackRepository.save(feedback);
    }


    @Override
    public void delete(User modifier, Feedback feedback) {
        checkSelfModifyPermissions(modifier, feedback.getGiver());

    }

    @Override
    public Double getAverageRatingForUser(User user) {
        return feedbackRepository.getAverageRatingForReceiver(user);
    }

    private void checkIfTravelPassed(Long travelId) {
        Travel travel = travelRepository.findById(travelId).orElseThrow();
        LocalDateTime departureTime = travel.getDepartureTime();
        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isBefore(departureTime)) {
            throw new TravelException(TRAVEL_NOT_COMPLETED_YET_ERROR);
        }
    }
    private void checkIfIsPassenger(User user, Travel travel) {
        List<Candidates> applications = candidateRepository.findByTravelAndStatus(travel, CandidateStatus.ACCEPTED);
        List<User> passengers = new ArrayList<>();
        for (Candidates application : applications
        ) {
            passengers.add(application.getUser());
        }
        if (passengers.isEmpty()) {
            throw new AuthorizationException(RECEIVER_IS_NOT_TRAVEL_PASSENGER_ERROR );
        }
    }

    private void checkIfIsDriver(User user, Travel travel) {
        if (!travel.getUserId().equals(user)) {
            throw new TravelException(GIVER_IS_NOT_TRAVEL_DRIVER_ERROR );
        }
    }
    private void checkDuplicateFeedback(Feedback feedback) {
        if (feedbackRepository.findByGiverAndReceiverAndTravel(feedback.getGiver(), feedback.getReceiver(),
                feedback.getTravel()).isPresent()) {
            throw new EntityDuplicateException(FEEDBACK_ALREADY_GIVEN_ERROR);
        }
    }
    private void checkSelfModifyPermissions(User modifier, User user) {
        if (!modifier.equals(user)) {
            throw new AuthorizationException(DELETE_ONLY_GIVEN_FEEDBACKS_ERROR);
        }
    }
}
