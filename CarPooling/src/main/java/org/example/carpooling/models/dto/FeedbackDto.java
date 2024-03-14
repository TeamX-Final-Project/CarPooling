package org.example.carpooling.models.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FeedbackDto {

    @NotNull(message = "Rating can't be empty")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating can't be more than 5")
    private int rating;

    @NotEmpty(message = "Comment can't be empty")
    @Size(max = 1000, message = "Comment shouldn't be more than 1000 chars long")
    private String comment;
}

