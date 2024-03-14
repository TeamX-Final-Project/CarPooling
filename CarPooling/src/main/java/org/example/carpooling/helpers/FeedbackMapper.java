package org.example.carpooling.helpers;

import lombok.AllArgsConstructor;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.FeedbackDto;
import org.example.carpooling.services.contracts.FeedbackService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FeedbackMapper {

    private final UserService userService;
    private final FeedbackService feedbackService;
    public Feedback fromFeedbackDto(FeedbackDto feedbackDto, User fromUser, User toUser, Travel travel){
        Feedback feedback = new Feedback();
        feedback.setComment(feedbackDto.getComment());
        feedback.setRating(feedbackDto.getRating());
        feedback.setGiver(fromUser);
        feedback.setReceiver(toUser);
        feedback.setTravel(travel);
        return feedback;
    }

}
