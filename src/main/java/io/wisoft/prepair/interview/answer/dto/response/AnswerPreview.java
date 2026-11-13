package io.wisoft.prepair.interview.answer.dto.response;

import io.wisoft.prepair.interview.answer.entity.Answer;

import java.time.Instant;
import java.util.UUID;

public record AnswerPreview(
        UUID id,
        Integer score,
        String answer,
        Instant createdAt
) {
    public static AnswerPreview from(Answer answer) {
        return new AnswerPreview(
                answer.getId(),
                answer.getScore(),
                answer.getAnswer(),
                answer.getCreatedAt()
        );
    }
}
