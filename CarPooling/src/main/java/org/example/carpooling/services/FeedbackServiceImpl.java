package org.example.carpooling.services;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.contracts.FeedbackRepository;
import org.example.carpooling.services.contracts.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {


    private final FeedbackRepository feedbackRepository;

@Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }


    @Override
    public Feedback getById(Long id) {
    return feedbackRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Feedback> findAllToUser(User user) {
        return feedbackRepository.findAllToUser(user);
    }
    @Override
    public List<Feedback> findAllFromUser(User user) {
        return feedbackRepository.findAllFromUser(user);
    }



}
