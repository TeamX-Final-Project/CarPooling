package org.example.carpooling.models.dto;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.CandidateStatus;

public class CandidateDto {

    private long id;

    private CandidateStatus status;

    private Travel travel;

    private User user;

    private boolean hasGivenFeedback;

    public CandidateDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CandidateStatus getStatus() {
        return status;
    }

    public void setStatus(CandidateStatus status) {
        this.status = status;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isHasGivenFeedback() {
        return hasGivenFeedback;
    }

    public void setHasGivenFeedback(boolean hasGivenFeedback) {
        this.hasGivenFeedback = hasGivenFeedback;
    }
}
