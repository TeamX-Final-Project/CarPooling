package org.example.carpooling.services.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;

import java.util.Optional;

public interface CandidateService {

    Candidates findById(long id);

    Candidates applyTravel(long id, User userToApply);

    Candidates approveTravel(long id, User userToConfirmApprove, Candidates userToApprove);

    Optional<Candidates> checkAppliedUsers(User userToApply, Travel travelToApply);

//    Optional<Candidates> checkPendingAndApprovedUsers(User userToApply, Travel travelToApply);

}
