package io.wisoft.prepair.interview.answer.dto.internal;

import io.wisoft.prepair.interview.answer.entity.InterviewFeedback;

public record AnswerSubmitResult(
        InterviewFeedback feedback,
        boolean firstAnswer
) {
}
