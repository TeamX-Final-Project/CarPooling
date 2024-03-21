package org.example.carpooling.models.dto;

import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;

import java.time.LocalDateTime;

public class SimpleTravelDto {

    private long travelId;

    private User userId;

    private String startPoint;

    private String endPoint;

    private LocalDateTime departureTime;

    private int freeSpots;

    private CandidateStatus currentUserStatus;

    public SimpleTravelDto() {
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public User getUserId() {
        return userId;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public CandidateStatus getCurrentUserStatus() {
        return currentUserStatus;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public void setCurrentUserStatus(CandidateStatus currentUserStatus) {
        this.currentUserStatus = currentUserStatus;
    }
}
