package io.wisoft.prepair.interview.external.openai.dto;

import java.util.List;

public record ComparisonQuestion(
        int index,
        String question,
        List<String> similarQuestions
) {
}
