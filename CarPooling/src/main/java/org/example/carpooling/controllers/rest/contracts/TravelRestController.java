package org.example.carpooling.controllers.rest.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.carpooling.controllers.rest.TravelRestControllerImpl;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.dto.TravelDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TravelRestController {

    @Operation(
            summary = "Get all travels",
            description = "fetches all travels entities and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping
    List<TravelDto> getAllTravels(@RequestParam(defaultValue = TravelRestControllerImpl.PAGE_NUMBER) int page,
                                                  @RequestParam(defaultValue = TravelRestControllerImpl.SIZE_PAGE) int size,
                                                  @RequestParam(required = false) String keyword,
                                                  @RequestParam(required = false) String sortBy,
                                                  @RequestParam(defaultValue = "ASC") String orderBy);

    @Operation(
            summary = "Get travel by id",
            description = "fetches all travel entities and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/{id}")
    Travel getTravelById(@RequestHeader HttpHeaders headers, @PathVariable int id);

    @Operation(
            summary = "Create travel",
            description = "Driver create travel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping
    Travel createTravel(@RequestHeader HttpHeaders headers, @Valid @RequestBody TravelDto travelDto);

    @Operation(
            summary = "Update travel",
            description = "Driver can upgrade his travel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PutMapping("/{id}")
    Travel updateTravel(@RequestHeader HttpHeaders headers,
                        @PathVariable long id,
                        @Valid @RequestBody TravelDto travelDto);

    @Operation(
            summary = "Delete travel by id",
            description = "Driver/Admin can delete travel by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PutMapping("/delete:{id}")
    Travel deleteTravelById(@RequestHeader HttpHeaders headers,
                            @PathVariable long id);

    @Operation(
            summary = "Cancel travel by id",
            description = "Driver can cancel his travel by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PutMapping("/cancel:{id}")
    Travel cancelTravel(@RequestHeader HttpHeaders headers, @PathVariable long id);
}
