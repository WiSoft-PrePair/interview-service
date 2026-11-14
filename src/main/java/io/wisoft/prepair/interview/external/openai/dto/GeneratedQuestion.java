package io.wisoft.prepair.interview.external.openai.dto;

import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;

public record GeneratedQuestion(
        String question,
        QuestionTag questionTag
) {
}
