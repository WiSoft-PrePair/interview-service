package io.wisoft.prepair.interview.question.dto.request;

import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import jakarta.validation.constraints.NotNull;

public record QuestionAssignRequest(
        @NotNull(message = "카테고리를 선택해 주세요.")
        QuestionTag tag
) {
}
