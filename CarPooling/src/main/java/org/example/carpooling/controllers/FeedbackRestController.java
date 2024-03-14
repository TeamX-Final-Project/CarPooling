package org.example.carpooling.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.TravelException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.FeedbackMapper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.FeedbackDto;
import org.example.carpooling.services.contracts.FeedbackService;
import org.example.carpooling.services.contracts.TravelService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/feedbacks")
@AllArgsConstructor
public class FeedbackRestController {

    private final FeedbackService feedbackService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final TravelService travelService;
    private final FeedbackMapper feedbackMapper;

    @GetMapping("/{id}")
    public Feedback getById(@PathVariable long id) {

        try {
            return feedbackService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    //todo - must change the methods in userService(getById must be public and override) and travelService (long id)
//    @PostMapping("/travel/{travelId}/user/{userId}")
//    public Feedback create(
//            @RequestHeader HttpHeaders headers,
//            @PathVariable Long travelId,
//            @PathVariable Long userId,
//            @Valid @RequestBody FeedbackDto feedbackDto){
//        try {
//            User giver = authenticationHelper.tryGetUser(headers);
//            User receiver = userService.getUserById(userId);
//           Travel travel = travelService.getById(travelId);
//            Feedback feedback = feedbackMapper.fromFeedbackDto (feedbackDto,giver,receiver,travel);
//            feedbackService.create(feedback);
//            return feedback;
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    e.getMessage());
//        } catch (EntityDuplicateException | TravelException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED,
//                    e.getMessage());
//        }
//    }
}
