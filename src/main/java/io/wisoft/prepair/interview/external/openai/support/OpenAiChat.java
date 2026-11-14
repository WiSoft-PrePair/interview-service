package io.wisoft.prepair.interview.external.openai.support;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OpenAiChat {

    private final ChatClient chatClient;

    public OpenAiChat(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public <T> T call(CallType type, String system, String user, ParameterizedTypeReference<T> responseType) {
        long start = System.nanoTime();

        ResponseEntity<ChatResponse, T> result = chatClient.prompt()
                .system(system)
                .user(user)
                .call()
                .responseEntity(responseType);

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
}
