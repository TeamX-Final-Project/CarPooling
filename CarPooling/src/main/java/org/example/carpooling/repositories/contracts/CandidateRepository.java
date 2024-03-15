package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.enums.CandidateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CandidateRepository extends JpaRepository<Candidates, Long> {

    List<Candidates> findByTravelAndStatus(Travel travel, CandidateStatus status);
}
