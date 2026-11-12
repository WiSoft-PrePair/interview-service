package io.wisoft.prepair.interview.legacy.dto;

import io.wisoft.prepair.interview.legacy.entity.InterviewQuestion;
import io.wisoft.prepair.interview.question.entity.enums.QuestionStatus;
import io.wisoft.prepair.interview.legacy.entity.enums.QuestionType;

import java.time.Instant;
import java.util.UUID;

public record QuestionResponse(
        UUID id,
        String question,
        String questionTag,
        QuestionType questionType,
        QuestionStatus status,
        Integer latestScore,
        UUID sessionId,
        Instant createdAt
) {

    public static QuestionResponse from(InterviewQuestion question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestion(),
                question.getQuestionTag(),
                question.getQuestionType(),
                question.getStatus(),
                question.getLatestScore(),
                question.getInterviewSession() != null ? question.getInterviewSession().getId() : null,
                question.getCreatedAt()
        );
    }
}
