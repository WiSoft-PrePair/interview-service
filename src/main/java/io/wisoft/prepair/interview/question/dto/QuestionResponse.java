package io.wisoft.prepair.interview.question.dto;

import io.wisoft.prepair.interview.question.entity.InterviewQuestion;
import io.wisoft.prepair.interview.question.entity.QuestionStatus;
import io.wisoft.prepair.interview.question.entity.QuestionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record QuestionResponse(
        UUID id,
        String question,
        String questionTag,
        QuestionType questionType,
        QuestionStatus status,
        Integer latestScore,
        UUID sessionId,
        LocalDateTime createdAt
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
