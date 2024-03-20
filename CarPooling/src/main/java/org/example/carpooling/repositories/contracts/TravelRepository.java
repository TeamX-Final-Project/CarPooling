package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;
import org.example.carpooling.models.enums.TravelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

//    @Query("select t from Travel t where t.travelStatus = 'AVAILABLE'")
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

    @Query("select COUNT (t) from Travel t where t.travelStatus= :travelStatus")
    int countCompletedTravels(@Param("travelStatus") TravelStatus travelStatus);


}
