package org.example.carpooling.services.contracts;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;

import java.util.List;

public interface FeedbackService {
    Feedback getById(Long id);

    List<Feedback> findAllToUser(User user);

    List<Feedback> findAllFromUser(User user);
}
