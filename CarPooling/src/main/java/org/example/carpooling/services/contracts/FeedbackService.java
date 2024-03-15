package org.example.carpooling.services.contracts;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;

public interface FeedbackService {
    Feedback getById(Long id);

    void create(Feedback feedback);
    void delete(User modifier, Feedback feedback);
}
