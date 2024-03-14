package org.example.carpooling.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.carpooling.helpers.AuthorizationHelper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.contracts.FeedbackRepository;
import org.example.carpooling.services.contracts.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Getter
@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    public static final String DELETE_ONLY_GIVEN_FEEDBACKS_ERROR = "You should be an author of the feedback";


    private final FeedbackRepository feedbackRepository;
    private final AuthorizationHelper authorizationHelper;

    @Override
    public Feedback getById(Long id) {
        return feedbackRepository.findById(id).orElseThrow();
    }


    public List<Feedback> getByReceiver(User user) {
        return feedbackRepository.findAllByReceiver(user);
    }

    @Override
    public Feedback create(Feedback feedback) {
//        authorizationHelper.checkIfTravelPassed(feedback.getTravel().getId(),
//                TRAVEL_NOT_COMPLETED_YET_ERROR);
//    return feedbackRepository.save(feedback);}

return feedbackRepository.save(feedback);
    }
    public void delete(User modifier,Feedback feedback){
        authorizationHelper.checkSelfModifyPermissions(
                modifier,
                feedback.getGiver(),
                DELETE_ONLY_GIVEN_FEEDBACKS_ERROR);

    }
}
