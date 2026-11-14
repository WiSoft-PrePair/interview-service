package io.wisoft.prepair.interview.external.openai.client;

import com.openai.errors.OpenAIException;
import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.openai.support.CallType;
import io.wisoft.prepair.interview.external.openai.support.OpenAiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiEmbeddingClient {

    private final EmbeddingModel embeddingModel;
    private final OpenAiProperties properties;

    public float[] embed(String text) {
        return embed(List.of(text))
                .getFirst();
    }

    public List<float[]> embed(List<String> texts) {
        long start = System.nanoTime();

        EmbeddingResponse response = request(texts, start);

        log.info("OpenAI 호출 완료: type={}, durationMs={}, inputTokens={}, count={}",
                CallType.EMBEDDING,
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start),
                response.getMetadata().getUsage().getPromptTokens(),
                texts.size());

        return response.getResults().stream()
                .map(Embedding::getOutput)
                .toList();
    }

    // 타임아웃을 적용해 임베딩을 요청하고, 호출 실패는 OPENAI_API_ERROR로 바꿈
    private EmbeddingResponse request(List<String> texts, long start) {
        try {
            return embeddingModel.call(new EmbeddingRequest(
                    texts,
                    OpenAiEmbeddingOptions.builder()
                            .timeout(properties.timeouts().get(CallType.EMBEDDING))
                            .build()));

        } catch (OpenAIException e) {
            log.warn("OpenAI 호출 실패: type={}, durationMs={}, error={}",
                    CallType.EMBEDDING,
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start),
                    e.getClass().getSimpleName());

            throw new BusinessException(ErrorCode.OPENAI_API_ERROR, e);
        }
    }
}
