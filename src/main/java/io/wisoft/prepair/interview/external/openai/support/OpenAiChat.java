package io.wisoft.prepair.interview.external.openai.support;

import com.openai.errors.OpenAIException;
import com.openai.models.completions.CompletionUsage;
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
import tools.jackson.core.JacksonException;

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

        log.info("OpenAI 호출 완료: type={}, durationMs={}, inputTokens={}, outputTokens={}, reasoningTokens={}",
                type,
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start),
                usage.getPromptTokens(),
                usage.getCompletionTokens(),
                reasoningTokens(usage));

        return entity;
    }

    private <T> ResponseEntity<ChatResponse, T> request(
            CallType type, String system, String user, ParameterizedTypeReference<T> responseType, long start
    ) {
        try {
            return chatClient.prompt()
                    .options(OpenAiChatOptions.builder().timeout(type.getTimeout()))
                    .system(system)
                    .user(user)
                    .call()
                    .responseEntity(responseType, ChatClient.EntityParamSpec::useProviderStructuredOutput);

        } catch (OpenAIException e) {
            log.warn("OpenAI 호출 실패: type={}, durationMs={}, error={}",
                    type,
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start),
                    e.getClass().getSimpleName());

            throw new BusinessException(ErrorCode.OPENAI_API_ERROR, e);

        } catch (JacksonException e) {
            log.warn("OpenAI 응답 변환 실패: type={}, durationMs={}",
                    type,
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));

            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE, e);
        }
    }

    // 추론 모델이 답변 전에 사용한 토큰 수, OpenAI 응답이 아니거나 값이 없으면 null
    private Long reasoningTokens(Usage usage) {
        if (usage.getNativeUsage() instanceof CompletionUsage nativeUsage) {
            return nativeUsage.completionTokensDetails()
                    .flatMap(CompletionUsage.CompletionTokensDetails::reasoningTokens)
                    .orElse(null);
        }
        return null;
    }
}
