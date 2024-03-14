package org.example.carpooling.helpers;

import lombok.AllArgsConstructor;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.TravelException;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidatesStatus;
import org.example.carpooling.repositories.contracts.CandidateRepository;
import org.example.carpooling.repositories.contracts.FeedbackRepository;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.example.carpooling.repositories.contracts.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component

public class AuthorizationHelper {
    public static final String FEEDBACK_ALREADY_GIVEN_ERROR = "You already gave your feedback";


    private final UserRepository userRepository;
    private final TravelRepository travelRepository;
    private final CandidateRepository candidateRepository;
    private final FeedbackRepository feedbackRepository;

    public void checkIfIsDriver(User user, Travel travel, String errorMessage) {
        if (!travel.getUserId().equals(user)) {
            throw new TravelException(errorMessage);
        }
    }

    public void checkIfIsPassenger(User user, Travel travel, String errorMessage) {
        List<Candidates> applications = candidateRepository.findByTravelAndStatus(travel, CandidatesStatus.ACCEPTED);
        List<User> passengers = new ArrayList<>();
        for (Candidates application : applications
        ) {
            passengers.add(application.getUser());
        }
        if (!passengers.contains(user)) {
            throw new AuthorizationException(errorMessage);
        }
    }

    public void checkDuplicateFeedback(Feedback feedback) {
        if (feedbackRepository.findByGiverAndReceiverAndTravel(feedback.getGiver(), feedback.getReceiver(),
                feedback.getTravel()).isPresent()) {
            throw new EntityDuplicateException(FEEDBACK_ALREADY_GIVEN_ERROR);
        }
    }

    public boolean isFeedbackGiven(User fromUser, User toUser, Travel travel) {
        return feedbackRepository.findByGiverAndReceiverAndTravel(fromUser, toUser, travel).isPresent();
    }
    public void checkSelfModifyPermissions(User modifier, User user,String errorMessage) {
        if (!modifier.equals(user)) {
            throw new AuthorizationException(errorMessage);
        }
    }

}
//public void checkIfTravelPassed(Long travelId, String errorMessage){
//    Travel travel = travelRepository.getById(travelId);
//    LocalDateTime arrivalTime = travel.getArrivalTime();
//    LocalDateTime currentTime = LocalDateTime.now();
//
//    if(currentTime.isBefore(arrivalTime)){
//        throw new TravelExceptions(errorMessage);
//    }


