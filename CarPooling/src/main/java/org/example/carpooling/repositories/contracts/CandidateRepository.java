package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Candidates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidates,Long> {

}
