package org.example.carpooling.services.contracts;

import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.User;

public interface CandidateService {

    Candidates findById(long id);

    Candidates applyTravel(long id, User userToApply);

    Candidates approveTravel(long id, User userToConfirmApprove, Candidates userToApprove);

}
