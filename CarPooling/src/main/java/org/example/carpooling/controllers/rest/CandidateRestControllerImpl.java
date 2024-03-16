package org.example.carpooling.controllers.rest;

import org.example.carpooling.controllers.rest.contracts.CandidateRestController;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.BlockedUserException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.User;
import org.example.carpooling.services.contracts.CandidateService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/candidates")
public class CandidateRestControllerImpl implements CandidateRestController {

    private final AuthenticationService authenticationService;

    private final CandidateService candidateService;

    private final UserService userService;

    @Autowired
    public CandidateRestControllerImpl(AuthenticationService authenticationService, CandidateService candidateService,
                                       UserService userService) {
        this.authenticationService = authenticationService;
        this.candidateService = candidateService;
        this.userService = userService;
    }

    @Override
    @PostMapping("/apply:{id}")
    public Candidates applyTravel(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User userToApply = authenticationService.tryGetUser(headers);
            return candidateService.applyTravel(id, userToApply);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    @PostMapping("/approve:{userId}/travel:{travelId}")
    public Candidates approveTravel(@RequestHeader HttpHeaders headers,
                                    @PathVariable long userId,
                                    @PathVariable long travelId) {
        try {
            User userToConfirmApprove = authenticationService.tryGetUser(headers);
            Candidates userToApprove = candidateService.findById(userId);
            return candidateService.approveTravel(travelId, userToConfirmApprove, userToApprove);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
