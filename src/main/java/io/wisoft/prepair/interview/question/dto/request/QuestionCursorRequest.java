package io.wisoft.prepair.interview.question.dto.request;

import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record QuestionCursorRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate cursor,

        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        @Max(value = 20, message = "페이지 크기는 20 이하여야 합니다.")
        Integer size,

        QuestionTag tag,

        @Size(max = 50, message = "검색어는 50자 이하여야 합니다.")
        String keyword
) {
    public QuestionCursorRequest {
        size = size == null ? 10 : size;
    }
}
