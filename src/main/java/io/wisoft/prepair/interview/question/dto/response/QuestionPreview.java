package io.wisoft.prepair.interview.question.dto.response;

import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.enums.QuestionStatus;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;

import java.time.LocalDate;
import java.util.UUID;

public record QuestionPreview(
        UUID id,
        QuestionTag tag,
        String question,
        QuestionStatus status,
        Integer bestScore,
        LocalDate assignedDate
) {
    public static QuestionPreview from(Question question) {
        return new QuestionPreview(
                question.getId(),
                question.getQuestionTag(),
                question.getQuestion(),
                question.getStatus(),
                question.getBestScore(),
                question.getAssignedDate()
        );
    }
}
