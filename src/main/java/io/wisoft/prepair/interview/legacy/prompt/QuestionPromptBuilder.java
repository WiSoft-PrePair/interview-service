package io.wisoft.prepair.interview.legacy.prompt;

import org.springframework.stereotype.Component;

@Component
public class QuestionPromptBuilder {

    public String buildVideoQuestionPrompt(String job, int count) {
        return """
                너는 %s 직무의 실제 면접을 진행하는 면접관이야.

                아래 직무에 맞는 기술 면접 질문 %d개를 만들고, 각 질문에 관련 키워드 태그 2-3개를 추출해줘.

                규칙:
                1. 해당 직무의 핵심 기술과 개념을 반드시 반영해줘.
                2. 질문들이 서로 중복되지 않도록 다양한 주제를 다뤄줘.
                3. 반드시 아래 JSON 형식으로만 응답하고, 다른 텍스트는 포함하지 마.
                4. JSON 외의 마크다운(```json 등)도 포함하지 마.
                [
                  {
                    "question": "질문 내용",
                    "tags": ["태그1", "태그2", "태그3"]
                  }
                ]
                """.formatted(job, count);
    }
}
