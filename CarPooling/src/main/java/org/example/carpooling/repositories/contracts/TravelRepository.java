package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    //ToDo implement filterOptions as parameter of the method
    Page<Travel> findAll(Specification<Travel> specification, Pageable pageable);

    Travel findById(long id);

//    Travel create(Travel travel);
//
//    Travel update(Travel travelToUpdate);
//
//    Travel deleteTravelById(Travel travelToDelete);
//
//    Travel cancel(Travel travelToCancel);
//
//    Candidates applyTravel(Candidates candidate, Travel travelToApply);
//    long count();
}
