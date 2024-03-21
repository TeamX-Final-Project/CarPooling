package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
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
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private static final String DELETE_ONLY_GIVEN_FEEDBACKS_ERROR = "You should be an author of the feedback";
    private static final String TRAVEL_NOT_COMPLETED_YET_ERROR = "Travel hasn't been completed yet";
    static final String FEEDBACK_ALREADY_GIVEN_ERROR = "You already gave your feedback";
    public static final String YOU_CANNOT_LEAVE_FEEDBACK_IF_YOU_ARE_NOT_PARTICIPANT_IN_THE_TRAVEL = "You cannot leave feedback if you are not participant in the travel.";

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
    public void create(Feedback feedback) {
        User currentUser = feedback.getGiver();
        checkIfTravelPassed(feedback.getTravel());
        checkIfCurrentUserIsParticipant(feedback, currentUser);
        checkDuplicateFeedback(feedback);
        feedbackRepository.save(feedback);
    }

    private void checkIfCurrentUserIsParticipant(Feedback feedback, User currentUser) {
        if (!currentUserIsDriver(currentUser, feedback.getTravel()) && !currentUserIsPassenger(feedback.getReceiver(), feedback.getTravel())) {
            throw new OperationNotAllowedException(YOU_CANNOT_LEAVE_FEEDBACK_IF_YOU_ARE_NOT_PARTICIPANT_IN_THE_TRAVEL);
        }
    }

    @Override
    public void delete(User modifier, Feedback feedback) {
        checkSelfModifyPermissions(modifier, feedback.getGiver());

    }

    @Override
    public Double getAverageRatingForUser(User user) {
        return feedbackRepository.getAverageRatingForReceiver(user);
    }

    @Override
    public boolean hasUserGivenFeedbackForCandidate(User currentUser, Candidates candidate) {
        return feedbackRepository.findByGiverAndReceiverAndTravel
                (currentUser, candidate.getUser(), candidate.getTravel()).isPresent();

    }

    private void checkIfTravelPassed(Travel travel) {
        LocalDateTime departureTime = travel.getDepartureTime();
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(departureTime)) {
            throw new OperationNotAllowedException(TRAVEL_NOT_COMPLETED_YET_ERROR);
        }
    }

    private boolean currentUserIsPassenger(User user, Travel travel) {
        List<Candidates> applications = candidateRepository.findByTravelAndStatus(travel, CandidateStatus.ACCEPTED);
        return applications.stream().anyMatch(candidates -> candidates.getUser().equals(user));
    }

    private boolean currentUserIsDriver(User user, Travel travel) {
        return travel.getUserId().equals(user);
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
