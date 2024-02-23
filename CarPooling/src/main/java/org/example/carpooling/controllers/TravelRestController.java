package org.example.carpooling.controllers;

import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {

    private final TravelService travelService;

    @Autowired
    public TravelRestController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping
    public List<Travel> getAllTravels(@RequestParam(required = false) String title,
                                      @RequestParam(required = false) String startPoint,
                                      @RequestParam(required = false) String endPoint,
                                      @RequestParam(required = false) String createdBy,
                                      @RequestParam(required = false) String sortBy,
                                      @RequestParam(required = false) String orderBy) {
        try {
            TravelFilterOptions travelFilterOptions = new TravelFilterOptions(
                    title,
                    startPoint,
                    endPoint,
                    createdBy,
                    sortBy,
                    orderBy);
            return travelService.getAllTravels(travelFilterOptions);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Travel getTravelById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            //ToDo implement logic to authenticate user in order to get the trips created by other users with id
            //ToDo catch also authorization exception if the user is not authenticated not to be able to search for trips
            return travelService.getById(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Travel createTravel() {

    }
}
