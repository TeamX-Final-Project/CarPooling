package org.example.carpooling.services.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;

import java.util.List;
import java.util.Optional;

public interface CandidateService {

    Candidates findById(long candidateId);

    Candidates applyTravel(long id, User userToApply);

    Candidates approveTravel(long id, User userToConfirmApprove, Candidates userToApprove);

    Candidates rejectTravel(long id, User userToConfirmReject, Candidates userToReject);

    Optional<Candidates> checkAppliedUsers(User userToApply, Travel travelToApply);

    List<Candidates> checkPendingAndApprovedUsers(User userToApply, Travel travelToApply);



}
