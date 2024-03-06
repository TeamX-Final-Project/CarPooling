package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    List<Feedback> findAllFromUser(User user);

    List<Feedback> findAllToUser(User user);

}
