package io.wisoft.prepair.interview.answer.dto.internal;

public record FeedbackResult(
        String good,
        String improvement,
        String recommendation,
        Integer score
) {
}
