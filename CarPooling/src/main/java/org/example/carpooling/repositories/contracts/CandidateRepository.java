package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Candidates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidates, Long> {

    Candidates findById(long id);


}
