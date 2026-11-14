package io.wisoft.prepair.interview.external.openai.support;

import com.openai.errors.OpenAIException;
import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OpenAiChat {

    private final ChatClient chatClient;
    private final OpenAiProperties properties;

    public OpenAiChat(ChatClient.Builder builder, OpenAiProperties properties) {
        this.chatClient = builder.build();
        this.properties = properties;
    }

    public <T> T call(CallType type, String system, String user, ParameterizedTypeReference<T> responseType) {
        long start = System.nanoTime();

        ResponseEntity<ChatResponse, T> result = request(
                type,
                system,
                user,
                responseType,
                start
        );

        ChatResponse response = result.response();
        T entity = result.entity();

        if (response == null || entity == null) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

        Usage usage = response.getMetadata().getUsage();

        log.info("OpenAI 호출 완료: type={}, durationMs={}, inputTokens={}, outputTokens={}",
                type,
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start),
                usage.getPromptTokens(),
                usage.getCompletionTokens());

        return entity;
    }

    // 호출 종류별 타임아웃을 적용해 요청하고, 호출 실패는 OPENAI_API_ERROR로 바꿈
    private <T> ResponseEntity<ChatResponse, T> request(
            CallType type, String system, String user, ParameterizedTypeReference<T> responseType, long start
    ) {
        try {
            return chatClient.prompt()
                    .options(OpenAiChatOptions.builder()
                            .timeout(properties.timeouts().get(type)))
                    .system(system)
                    .user(user)
                    .call()
                    .responseEntity(responseType);

        } catch (OpenAIException e) {
            log.warn("OpenAI 호출 실패: type={}, durationMs={}, error={}",
                    type,
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start),
                    e.getClass().getSimpleName());

            throw new BusinessException(ErrorCode.OPENAI_API_ERROR, e);
        }
    }
}
