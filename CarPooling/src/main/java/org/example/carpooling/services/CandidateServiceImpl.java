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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    public static final String YOU_ARE_NOT_ALLOWED_TO_APPLY_FOR_TRAVEL_ERROR = "You are not allowed to apply for this travel";

    public static final String YOU_ARE_THE_CREATOR_OF_THE_TRAVEL = "You are not allowed to apply for this travel since you created the travel";

    public static final String YOU_ARE_NOT_ALLOWED_TO_APPROVE_FOR_TRAVEL_ERROR = "You are not allowed to approve for this travel";
    public static final String YOU_CAN_T_APPLY_TO_THE_SAME_TRAVEL_TWICE_ERROR = "You can't apply to the same travel twice";

    private final CandidateRepository candidateRepository;

    private final TravelService travelService;

    public CandidateServiceImpl(CandidateRepository candidateRepository, TravelService travelService) {
        this.candidateRepository = candidateRepository;
        this.travelService = travelService;
    }


    @Override
    public Candidates findById(long candidateId) {
        return candidateRepository.findById(candidateId).orElseThrow();
    }

    @Override
    public Candidates applyTravel(long id, User userToApply) {
        Travel travelToApply = travelService.getById(id, userToApply);
        checkApplyPermission(userToApply, travelToApply);
        checkTravelStatusApply(travelToApply);
        Optional<Candidates> existentCandidate = candidateRepository.findByUserAndTravel(userToApply, travelToApply);
        if (existentCandidate.isPresent()) {
            throw new OperationNotAllowedException(YOU_CAN_T_APPLY_TO_THE_SAME_TRAVEL_TWICE_ERROR);
        }

        Candidates candidate = new Candidates();
        candidate.setStatus(CandidateStatus.PENDING);
        candidate.setUser(userToApply);
        candidate.setTravel(travelToApply);

        return candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidates> checkAppliedUsers(User userToApply, Travel travelToApply) {
        List<Candidates> appliedCandidates = candidateRepository.findByTravelAndStatusNot(travelToApply,
                CandidateStatus.REJECTED);
        return appliedCandidates.stream().filter(candidate -> candidate.getUser().equals(userToApply)).findFirst();
    }

    @Override
    public List<Candidates> checkPendingAndApprovedUsers(User userToConfirmApprove, Travel travelToConfirm) {
        if (userToConfirmApprove.getUserId() != travelToConfirm.getUserId().getUserId()) {
            return new ArrayList<>();
        }
        return candidateRepository.findCandidatesByTravel(travelToConfirm);
    }

    @Override
    public Candidates approveTravel(long id, User userToConfirmApprove, Candidates userToApprove) {
        Travel travelToApprove = travelService.getById(id, userToConfirmApprove);
        int currentFreeSpots = travelToApprove.getFreeSpots();
        checkApprovePermission(userToConfirmApprove, travelToApprove);
        checkTravelStatusApprove(travelToApprove);

        if (currentFreeSpots == 0) {
            travelToApprove.setTravelStatus(TravelStatus.FULL);
        }
        userToApprove.setStatus(CandidateStatus.ACCEPTED);
        travelToApprove.setFreeSpots(--currentFreeSpots);
        return candidateRepository.save(userToApprove);
    }

    @Override
    public Candidates rejectTravel(long id, User userToConfirmReject, Candidates userToReject) {
        Travel travelToReject = travelService.getById(id, userToConfirmReject);

        checkApprovePermission(userToConfirmReject, travelToReject);
        checkTravelStatusApprove(travelToReject);

        userToReject.setStatus(CandidateStatus.REJECTED);
        return candidateRepository.save(userToReject);
    }


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
