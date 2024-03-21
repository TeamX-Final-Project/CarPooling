package org.example.carpooling.mappers;


import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.CandidateDto;
import org.example.carpooling.services.contracts.FeedbackService;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    private final FeedbackService feedbackService;

    public CandidateMapper(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public CandidateDto toDto(Candidates candidate, User currentUser) {
        CandidateDto dto = new CandidateDto();
        dto.setId(candidate.getId());
        dto.setStatus(candidate.getStatus());
        dto.setUser(candidate.getUser());
        dto.setTravel(candidate.getTravel());
        dto.setHasGivenFeedback(feedbackService.hasUserGivenFeedbackForCandidate(currentUser, candidate));
        return dto;
    }


}
