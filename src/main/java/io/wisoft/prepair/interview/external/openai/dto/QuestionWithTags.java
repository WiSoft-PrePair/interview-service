package io.wisoft.prepair.interview.external.openai.dto;

import java.util.List;

public record QuestionWithTags(
        String question,
        List<String> tags
) {
    public String joinTags() {
        return String.join(",", tags);
    }
}
