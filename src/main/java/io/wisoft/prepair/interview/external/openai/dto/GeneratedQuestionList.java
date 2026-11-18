package io.wisoft.prepair.interview.external.openai.dto;

import java.util.List;

public record GeneratedQuestionList(
        List<GeneratedQuestion> questions
) {
}
