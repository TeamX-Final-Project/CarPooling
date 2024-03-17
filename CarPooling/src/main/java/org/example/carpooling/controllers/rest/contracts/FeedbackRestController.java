package org.example.carpooling.controllers.rest.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.dto.FeedbackDto;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Feedback", description = "Feedback REST controller")
public interface FeedbackRestController {


    @Operation(
            summary = "Get feedback by id",
            description = "Fetches feedback of user by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/{id}")
    Feedback getById(@PathVariable long id);


    @Operation(
            summary = "Create feedback",
            description = "Creates feedback after travel is completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/travel/{travelId}/user/{userId}")
    Feedback create(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long travelId,
            @PathVariable Long userId,
            @Valid @RequestBody FeedbackDto feedbackDto);
}
