package org.example.carpooling.controllers;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.services.contracts.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackRestController {

    private final FeedbackService feedbackService;
@Autowired
    public FeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping ("/{id}")
        public Feedback getById (@PathVariable long id){
    return feedbackService.getById(id);
    }

}
