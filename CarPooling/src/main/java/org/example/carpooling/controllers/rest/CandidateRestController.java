package org.example.carpooling.controllers.rest;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.BlockedUserException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
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
public class CandidateRestController {

    private final AuthenticationHelper authenticationHelper;

    private final CandidateService candidateService;

    private final UserService userService;

    @Autowired
    public CandidateRestController(AuthenticationHelper authenticationHelper, CandidateService candidateService,
                                   UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.candidateService = candidateService;
        this.userService = userService;
    }

    @PostMapping("/apply:{id}")
    public Candidates applyTravel(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User userToApply = authenticationHelper.tryGetUser(headers);
            return candidateService.applyTravel(id, userToApply);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/approve:{userId}/travel:{travelId}")
    public Candidates approveTravel(@RequestHeader HttpHeaders headers,
                                    @PathVariable long userId,
                                    @PathVariable long travelId) {
        try {
            User userToConfirmApprove = authenticationHelper.tryGetUser(headers);
            Candidates userToApprove = candidateService.findById(userId);
            return candidateService.approveTravel(travelId, userToConfirmApprove, userToApprove);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
