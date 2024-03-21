package org.example.carpooling.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

public class FeedbackDto {

    @JsonIgnore
    private long id;

    @NotNull(message = "Rating can't be empty")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating can't be more than 5")
    private int rating;

    @NotEmpty(message = "Comment can't be empty")
    @Size(max = 1000, message = "Comment shouldn't be more than 1000 chars long")
    private String comment;

    public FeedbackDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}


