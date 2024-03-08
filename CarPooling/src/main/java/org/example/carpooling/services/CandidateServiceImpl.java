package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.repositories.contracts.CandidateRepository;
import org.example.carpooling.services.contracts.CandidateService;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.stereotype.Service;

@Service
public class CandidateServiceImpl implements CandidateService {

    public static final String YOU_ARE_NOT_ALLOWED_TO_APPLY_FOR_TRAVEL_ERROR = "You are not allowed to apply for this travel";

    public static final String YOU_ARE_THE_CREATOR_OF_THE_TRAVEL = "You are not allowed to apply for this travel since you created the travel";

    public static final String YOU_ARE_NOT_ALLOWED_TO_APPROVE_FOR_TRAVEL_ERROR = "You are not allowed to approve for this travel";


    private final CandidateRepository candidateRepository;

    private final TravelService travelService;

    public CandidateServiceImpl(CandidateRepository candidateRepository, TravelService travelService) {
        this.candidateRepository = candidateRepository;
        this.travelService = travelService;
    }


    @Override
    public Candidates findById(long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Candidates applyTravel(long id, User userToApply) {
        Travel travelToApply = travelService.getById(id);
        checkApplyPermission(userToApply, travelToApply);
        checkTravelStatusApply(travelToApply);
        Candidates candidate = new Candidates();
        candidate.setStatus(CandidateStatus.FOR_APPROVAL);
        candidate.setTravelId(id);
        candidate.setUserId(userToApply.getUserId());
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidates approveTravel(long id, User userToConfirmApprove, Candidates userToApprove) {
        Travel travelToApprove = travelService.getById(id);
        checkApprovePermission(userToConfirmApprove, travelToApprove);
        checkTravelStatusApprove(travelToApprove);
        userToApprove.setStatus(CandidateStatus.APPROVED);

        return candidateRepository.save(userToApprove);
    }
//TODO double check this part when applying for travel the logic need to be reworked
// so you can't apply for the same travel twice
    private void checkApplyPermission(User user, Travel travel) {

        if (user.getUserId() == travel.getUserId().getUserId()) {
            throw new AuthorizationException(YOU_ARE_THE_CREATOR_OF_THE_TRAVEL);
        }
    }

    private static void checkTravelStatusApply(Travel travelToApply) {
        if (!TravelStatus.AVAILABLE.equals(travelToApply.getTravelStatus())) {
            throw new OperationNotAllowedException(YOU_ARE_NOT_ALLOWED_TO_APPLY_FOR_TRAVEL_ERROR);
        }
    }

    private static void checkTravelStatusApprove(Travel travelToApply) {
        if (!TravelStatus.AVAILABLE.equals(travelToApply.getTravelStatus())) {
            throw new OperationNotAllowedException(YOU_ARE_NOT_ALLOWED_TO_APPROVE_FOR_TRAVEL_ERROR);
        }
    }

    private void checkApprovePermission(User user, Travel travel) {

        if (user.getUserId() != travel.getUserId().getUserId()) {
            throw new AuthorizationException(YOU_ARE_NOT_ALLOWED_TO_APPROVE_FOR_TRAVEL_ERROR);
        }
    }
}
