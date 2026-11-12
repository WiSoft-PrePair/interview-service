package io.wisoft.prepair.interview.legacy.session.dto.response;

import io.wisoft.prepair.interview.legacy.session.entity.SessionStatus;

import java.util.List;
import java.util.UUID;

public record SessionDetailResponse(
        UUID sessionId,
        SessionStatus status,
        Integer finalScore,
        String finalFeedback,
        List<QuestionFeedback> questions
) {
    public record QuestionFeedback(
            UUID questionId,
            String question,
            String mediaUrl,
            Integer combinedScore,
            String combinedFeedback,
            String sttFeedback,
            String videoFeedback
    ) {
    }
}
