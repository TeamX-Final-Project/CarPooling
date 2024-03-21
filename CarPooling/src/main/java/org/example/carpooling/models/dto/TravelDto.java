package org.example.carpooling.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.TravelStatus;

import java.time.LocalDateTime;

public class TravelDto {


    @JsonIgnore
    private long travelId;
    @NotNull(message = "Start point can't be empty")
    private String startPoint;
    @NotNull(message = "End point can't be empty")
    private String endPoint;
    @NotNull(message = "Departure time can't be empty")
    private LocalDateTime departureTime;
    @NotNull(message = "Free spots can't be empty")
    private int freeSpots;
    @JsonIgnore
    private User userId;
    @JsonIgnore
    private String creator;
    @NotNull(message = "Travel status can't be empty")
    private TravelStatus travelStatus;
    private String travelComment;


    public TravelDto() {
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
