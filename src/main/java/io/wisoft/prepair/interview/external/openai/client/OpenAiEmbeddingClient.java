package io.wisoft.prepair.interview.external.openai.client;

import io.wisoft.prepair.interview.external.openai.support.CallType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiEmbeddingClient {

    private final EmbeddingModel embeddingModel;

    public float[] embed(String text) {
        return embed(List.of(text))
                .getFirst();
    }

    public List<float[]> embed(List<String> texts) {
        long start = System.nanoTime();

        EmbeddingResponse response = embeddingModel.embedForResponse(texts);

        log.info("OpenAI 호출 완료: type={}, durationMs={}, inputTokens={}, count={}",
                CallType.EMBEDDING,
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start),
                response.getMetadata().getUsage().getPromptTokens(),
                texts.size());

        return response.getResults().stream()
                .map(Embedding::getOutput)
                .toList();
    }
}
