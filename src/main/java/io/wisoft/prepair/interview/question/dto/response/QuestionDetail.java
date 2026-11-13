package io.wisoft.prepair.interview.question.dto.response;

import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;

import java.time.LocalDate;
import java.util.UUID;

public record QuestionDetail(
        UUID id,
        QuestionTag tag,
        String question,
        LocalDate assignedDate
) {
    public static QuestionDetail from(Question question) {
        return new QuestionDetail(
                question.getId(),
                question.getQuestionTag(),
                question.getQuestion(),
                question.getAssignedDate()
        );
    }
}
