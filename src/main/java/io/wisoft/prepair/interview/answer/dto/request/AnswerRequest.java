package io.wisoft.prepair.interview.answer.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequest(
        @NotBlank String answer
) {
}
