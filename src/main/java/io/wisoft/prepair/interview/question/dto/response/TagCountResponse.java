package io.wisoft.prepair.interview.question.dto.response;

import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;

import java.util.Map;

public record TagCountResponse(
        long total,
        Map<QuestionTag, Long> counts
) {
    public static TagCountResponse of(long total, Map<QuestionTag, Long> counts) {
        return new TagCountResponse(total, counts);
    }
}
