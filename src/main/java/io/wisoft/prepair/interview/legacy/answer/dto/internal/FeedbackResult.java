package io.wisoft.prepair.interview.legacy.answer.dto.internal;

public record FeedbackResult(
        String good,
        String improvement,
        String recommendation,
        Integer score
) {
}
