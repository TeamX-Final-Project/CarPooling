package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;
import org.example.carpooling.models.enums.TravelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Page<Travel> findAll(Specification<Travel> specification, Pageable pageable);

    Travel findById(long id);

    List<Travel> findByUserIdAndTravelStatus(User user, TravelStatus status);

    @Query("select COUNT (t) from Travel t where t.userId = :user and t.travelStatus = :status")
    int countCompletedTravelsAsDriver(@Param("user") User user, @Param("status") TravelStatus status);

    @Query("SELECT COUNT(t) FROM Travel t WHERE EXISTS (" +
            "SELECT 1 FROM Candidates c WHERE c.user = :user " +
            "AND c.status = :candidateStatus AND c.travel = t " +
            "AND t.travelStatus = :travelStatus)")
    int countCompletedTravelsAsPassenger(
            @Param("user") User user,
            @Param("candidateStatus") CandidateStatus candidateStatus,
            @Param("travelStatus") TravelStatus travelStatus
    );

    long countTravelByTravelStatus(TravelStatus travelStatus);

    @Query(nativeQuery = true, value = "select * from carpoolingx.travels order by travels.travel_id desc limit 10")
    List<Travel> getMostRecentTravels();
}
