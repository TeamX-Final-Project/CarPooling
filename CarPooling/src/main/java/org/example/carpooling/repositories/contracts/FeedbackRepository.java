package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;


public interface FeedbackRepository extends JpaRepository<Feedback,Long> {


    Optional<Feedback> findByGiverAndReceiverAndTravel(User giver, User receiver, Travel travel);
    List<Feedback> findAllByReceiver(User user);

    @Query("SELECT ROUND(AVG(f.rating), 1) FROM Feedback f WHERE f.receiver = :user")
    Double getAverageRatingForReceiver(@Param("user") User user);

}
