package org.example.carpooling.mappers;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.FeedbackDto;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public Feedback fromFeedbackDto(FeedbackDto feedbackDto, User fromUser, User toUser, Travel travel){
        Feedback feedback = new Feedback();
//        feedback.setId(feedbackDto.getId());
        feedback.setComment(feedbackDto.getComment());
        feedback.setRating(feedbackDto.getRating());
        feedback.setGiver(fromUser);
        feedback.setReceiver(toUser);
        feedback.setTravel(travel);
        return feedback;
    }

}

