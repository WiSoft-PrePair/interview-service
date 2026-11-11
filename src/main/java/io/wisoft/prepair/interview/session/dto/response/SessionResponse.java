package io.wisoft.prepair.interview.session.dto.response;

import io.wisoft.prepair.interview.session.entity.InterviewSession;
import io.wisoft.prepair.interview.session.entity.SessionStatus;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(
        UUID sessionId,
        SessionStatus status,
        Integer finalScore,
        int totalQuestionCount,
        Instant createdAt
) {
    public static SessionResponse from(InterviewSession session) {
        return new SessionResponse(
                session.getId(),
                session.getStatus(),
                session.getFinalScore(),
                session.getTotalQuestionCount(),
                session.getCreatedAt()
        );
    }
}
