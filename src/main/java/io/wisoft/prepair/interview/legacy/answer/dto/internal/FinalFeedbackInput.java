package io.wisoft.prepair.interview.legacy.answer.dto.internal;

import java.util.UUID;

public record FinalFeedbackInput(
        UUID questionId,
        String question,
        String combinedFeedback,
        int score
) {
}
