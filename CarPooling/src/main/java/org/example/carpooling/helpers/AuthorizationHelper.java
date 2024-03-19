package org.example.carpooling.helpers;

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
import org.example.carpooling.repositories.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorizationHelper {
    public static final String FEEDBACK_ALREADY_GIVEN_ERROR = "You already gave your feedback";


    private final UserRepository userRepository;
    private final TravelRepository travelRepository;
    private final CandidateRepository candidateRepository;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public AuthorizationHelper(UserRepository userRepository, TravelRepository travelRepository,
                               CandidateRepository candidateRepository, FeedbackRepository feedbackRepository) {
        this.userRepository = userRepository;
        this.travelRepository = travelRepository;
        this.candidateRepository = candidateRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public void checkIfTravelPassed(Long travelId, String errorMessage) {
        Travel travel = travelRepository.getById(travelId);
        LocalDateTime departureTime = travel.getDepartureTime();
        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isBefore(departureTime)) {
            throw new TravelException(errorMessage);
        }
    }
    public void checkIfIsPassenger(User user, Travel travel, String errorMessage) {
        List<Candidates> applications = candidateRepository.findByTravelAndStatus(travel, CandidateStatus.ACCEPTED);
        List<User> passengers = new ArrayList<>();
        for (Candidates application : applications
        ) {
            passengers.add(application.getUser());
        }
        if (!passengers.contains(user)) {
            throw new AuthorizationException(errorMessage);
        }
    }
    public void checkIfIsDriver(User user, Travel travel, String errorMessage) {
        if (!travel.getUserId().equals(user)) {
            throw new TravelException(errorMessage);
        }
    }
    public void checkDuplicateFeedback(Feedback feedback) {
        if (feedbackRepository.findByGiverAndReceiverAndTravel(feedback.getGiver(), feedback.getReceiver(),
                feedback.getTravel()).isPresent()) {
            throw new EntityDuplicateException(FEEDBACK_ALREADY_GIVEN_ERROR);
        }
    }
    public void checkSelfModifyPermissions(User modifier, User user, String errorMessage) {
        if (!modifier.equals(user)) {
            throw new AuthorizationException(errorMessage);
        }
    }

}
