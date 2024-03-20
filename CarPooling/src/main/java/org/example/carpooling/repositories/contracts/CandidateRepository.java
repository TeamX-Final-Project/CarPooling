package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface CandidateRepository extends JpaRepository<Candidates, Long> {

    List<Candidates> findByTravelAndStatus(Travel travel, CandidateStatus status);
    List<Candidates> findByTravelAndStatusNot(Travel travel, CandidateStatus status);
    Optional<Candidates> findByUser(User userId);
}
