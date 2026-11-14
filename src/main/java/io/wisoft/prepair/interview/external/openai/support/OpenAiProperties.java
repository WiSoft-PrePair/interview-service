package io.wisoft.prepair.interview.external.openai.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

@ConfigurationProperties("openai")
public record OpenAiProperties(Map<CallType, Duration> timeouts) {

    public OpenAiProperties {
        for (CallType type : CallType.values()) {
            if (timeouts == null || timeouts.get(type) == null) {
                throw new IllegalStateException(
                        "openai.timeouts." + type.name().toLowerCase(Locale.ROOT) + " 설정이 없습니다."
                );
            }
        }
    }
}
