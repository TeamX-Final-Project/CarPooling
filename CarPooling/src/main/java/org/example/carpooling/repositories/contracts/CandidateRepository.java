package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidatesStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidates,Long> {

    Candidates findByTravelAndAndUser (Travel travel, User user);

    List<Candidates> findByTravelAndStatus(Travel travel, CandidatesStatus status);


}
