package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.carpooling.models.enums.TravelStatus;

import java.sql.Timestamp;
import java.util.Objects;

public class TravelDto {

    @NotNull(message = "Start point can't be empty")
    private String startPoint;
    @NotNull(message = "End point can't be empty")
    private String endPoint;
    @NotNull(message = "Departure time can't be empty")
    private Timestamp departureTime;
    @NotNull(message = "Free spots can't be empty")
    private int freeSpots;
    @NotNull(message = "Travel status can't be empty")
    private TravelStatus travelStatus;

    private String travelComment;

    public TravelDto() {
    }


    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public TravelStatus getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(TravelStatus travelStatus) {
        this.travelStatus = travelStatus;
    }

    public String getTravelComment() {
        return travelComment;
    }

    public void setTravelComment(String travelComment) {
        this.travelComment = travelComment;
    }

}
