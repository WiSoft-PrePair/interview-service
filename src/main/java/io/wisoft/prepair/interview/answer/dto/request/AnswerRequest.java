package io.wisoft.prepair.interview.answer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnswerRequest(
        @NotBlank(message = "답변을 입력해 주세요.")
        @Size(max = 3000, message = "답변은 3000자 이하여야 합니다.")
        String answer
) {
}
