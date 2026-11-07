package io.wisoft.prepair.interview.answer.dto.internal;

import java.util.UUID;

public record FinalFeedbackInput(
        UUID questionId,
        String question,
        String combinedFeedback,
        int score
) {
}
