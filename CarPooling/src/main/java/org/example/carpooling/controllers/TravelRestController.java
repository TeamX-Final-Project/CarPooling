package org.example.carpooling.controllers;

import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {

    private TravelService travelService;

    @Autowired
    public TravelRestController(TravelService travelService) {
        this.travelService = travelService;
    }
}
