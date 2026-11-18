package io.wisoft.prepair.interview.external.openai.dto;

// 분류는 요청할 때 코드가 정하므로 응답에는 질문만 받음
public record GeneratedQuestion(
        String question
) {
}
