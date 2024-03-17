package org.example.carpooling.controllers.rest.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.carpooling.models.Candidates;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@Tag(name = "Candidate", description = "Candidate REST controller")
public interface CandidateRestController {

    @Operation(
            summary = "Apply Travel",
            description = "Candidate apply for travel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/apply:{id}")
    Candidates applyTravel(@RequestHeader HttpHeaders headers, @PathVariable long id);

    @Operation(
            summary = "Approve Travel",
            description = "Driver approve candidate for created travel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/approve:{userId}/travel:{travelId}")
    Candidates approveTravel(@RequestHeader HttpHeaders headers,
                             @PathVariable long userId,
                             @PathVariable long travelId);
}
