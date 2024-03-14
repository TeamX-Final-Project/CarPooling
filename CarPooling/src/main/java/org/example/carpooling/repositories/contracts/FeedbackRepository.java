package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface FeedbackRepository extends JpaRepository<Feedback,Long> {


    List<Feedback> findAllByReceiver(User user);


    Optional<Feedback> findByGiverAndReceiverAndTravel(User giver, User receiver, Travel travel);


}
