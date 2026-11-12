package io.wisoft.prepair.interview.question.dto;

import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;

public record TagCount(
        QuestionTag tag,
        long count
) {
}
