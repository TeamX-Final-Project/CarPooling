package org.example.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.carpooling.models.enums.TravelStatus;

import java.sql.Timestamp;

@Entity
@Table(name = "travels")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    @JsonIgnore
    private long travelId;
    @Column(name = "start_point")
    private String startPoint;
    @Column(name = "end_point")
    private String endPoint;
    @Column(name = "departure_time")
    private Timestamp departureTime;
    @Column(name = "free_spots")
    private int freeSpots;
    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_status")
    @JsonIgnore
    private TravelStatus travelStatus;
    @Column(name = "distance_travel")
    private int distanceTravel;
    @Column(name = "duration_travel")
    private int durationTravel;
    @Column(name = "comment_travel")
    private String travelComment;




//TODO implement the comment logic probably OneToMany
    // since one travel can have many comments for pets,luggage,smoking,etc...

    public Travel() {
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(int travelId) {
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public TravelStatus getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(TravelStatus travelStatus) {
        this.travelStatus = travelStatus;
    }

    public int getDistanceTravel() {
        return distanceTravel;
    }

    public void setDistanceTravel(int distanceTravel) {
        this.distanceTravel = distanceTravel;
    }

    public int getDurationTravel() {
        return durationTravel;
    }

    public void setDurationTravel(int durationTravel) {
        this.durationTravel = durationTravel;
    }
    public String getTravelComment() {
        return travelComment;
    }

    public void setTravelComment(String travelComment) {
        this.travelComment = travelComment;
    }
}
