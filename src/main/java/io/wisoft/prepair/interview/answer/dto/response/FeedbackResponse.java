package io.wisoft.prepair.interview.answer.dto.response;

import io.wisoft.prepair.interview.answer.dto.internal.FeedbackDetail;
import io.wisoft.prepair.interview.answer.entity.InterviewFeedback;
import io.wisoft.prepair.interview.answer.entity.FeedbackType;

import java.time.Instant;
import java.util.UUID;

public record FeedbackResponse(
        UUID id,
        String answer,
        FeedbackDetail feedback,
        FeedbackType feedbackType,
        Integer score,
        Instant createdAt
) {

    public static FeedbackResponse from(InterviewFeedback f, FeedbackDetail detail) {
        return new FeedbackResponse(
                f.getId(),
                f.getInterviewAnswer().getAnswer(),
                detail,
                f.getFeedbackType(),
                f.getScore(),
                f.getCreatedAt()
        );
    }
}
