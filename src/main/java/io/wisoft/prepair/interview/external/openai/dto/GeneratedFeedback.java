package io.wisoft.prepair.interview.external.openai.dto;

public record GeneratedFeedback(
        String good,
        String improvement,
        String example,
        String summary,
        Integer score
) {
}
