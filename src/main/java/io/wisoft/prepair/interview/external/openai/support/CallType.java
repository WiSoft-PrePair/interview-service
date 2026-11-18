package io.wisoft.prepair.interview.external.openai.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public enum CallType {

    // OpenAI 호출 종류 | 호출 로그에서 종류별로 구분하는 데 사용
    FEEDBACK(Duration.ofSeconds(20)),
    QUESTION(Duration.ofSeconds(120)),
    COMPARE(Duration.ofSeconds(120)),
    EMBEDDING(null);

    private final Duration timeout;
}
