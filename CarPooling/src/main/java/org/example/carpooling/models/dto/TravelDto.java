package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.Objects;

public class TravelDto {

    @NotEmpty
    @Size(min = 4, max = 50, message = "Title must be between 4 and 50 symbols")
    private String title;
    @NotEmpty
    @Size(min = 4, max = 20, message = "Start point must be between 4 and 20 symbols")
    private String startPoint;
    @NotEmpty
    @Size(min = 4, max = 20, message = "End point must be between 4 and 20 symbols")
    private String endPoint;
    @NotEmpty
    private Timestamp departureTime;
    @NotEmpty
    private int freeSpots;

    public TravelDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelDto travelDto = (TravelDto) o;
        return Objects.equals(title, travelDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
