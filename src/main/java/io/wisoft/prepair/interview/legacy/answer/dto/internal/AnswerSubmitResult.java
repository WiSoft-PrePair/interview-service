package io.wisoft.prepair.interview.legacy.answer.dto.internal;

import io.wisoft.prepair.interview.legacy.answer.entity.InterviewFeedback;

public record AnswerSubmitResult(
        InterviewFeedback feedback,
        boolean firstAnswer
) {
}
