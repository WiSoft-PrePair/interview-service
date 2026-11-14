package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.external.openai.client.OpenAiEmbeddingClient;
import io.wisoft.prepair.interview.external.openai.client.OpenAiQuestionClient;
import io.wisoft.prepair.interview.external.openai.dto.GeneratedQuestion;
import io.wisoft.prepair.interview.external.openai.dto.ComparisonQuestion;
import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.service.job.JobReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionPoolGenerator {

    // 질문 풀 보충 기준
    private static final int MIN_REMAINING_QUESTIONS = 3;
    private static final int TARGET_REMAINING_QUESTIONS = 7;

    // 중복 판정 후보 조회 기준
    private static final double MIN_SIMILARITY = 0.8;
    private static final int MAX_SIMILAR_QUESTIONS = 5;

    private final OpenAiQuestionClient questionClient;
    private final OpenAiEmbeddingClient embeddingClient;

    private final QuestionPoolReader poolReader;
    private final QuestionPoolWriter poolWriter;
    private final JobReader jobReader;

    public void execute() {
        long startedAt = System.nanoTime();

        for (Job job : jobReader.findAll()) {
            try {
                List<GeneratedQuestion> questions = generateQuestions(job);

                if (questions.isEmpty()) {
                    continue;
                }

                List<float[]> embeddings = embedQuestions(questions);
                List<ComparisonQuestion> comparisons = getComparisons(job, questions, embeddings);
                Set<Integer> duplicateIndexes = compareQuestions(comparisons);

                poolWriter.save(job, questions, embeddings, duplicateIndexes);

            } catch (Exception e) {
                log.error("질문 풀 보충 실패: jobId={}", job.getId(), e);
            }
        }

        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
        log.info("질문 풀 생성 완료: durationMs={}", durationMs);
    }

    // 01. 부족분 생성 | 회원별 최소 잔여가 3개 미만이면 7개까지 보충
    private List<GeneratedQuestion> generateQuestions(Job job) {
        Integer remaining = poolReader.getMinRemaining(job.getId());

        if (remaining == null || remaining >= MIN_REMAINING_QUESTIONS) {
            return List.of();
        }

        int neededCount = TARGET_REMAINING_QUESTIONS - remaining;
        return questionClient.generate(job.getName(), neededCount);
    }

    // 02. 임베딩 생성 | 새 질문들을 한 번에 벡터로 변환
    private List<float[]> embedQuestions(List<GeneratedQuestion> questions) {
        List<String> texts = questions.stream()
                .map(GeneratedQuestion::question)
                .toList();

        return embeddingClient.embed(texts);
    }

    // 03. 비교 대상 구성 | 같은 직무에서 유사도 0.8 이상인 기존 질문을 최대 5개 조회
    private List<ComparisonQuestion> getComparisons(
            Job job, List<GeneratedQuestion> questions, List<float[]> embeddings
    ) {
        List<ComparisonQuestion> comparisons = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            List<QuestionPool> similarQuestions = poolReader
                    .findSimilar(
                            job.getId(),
                            embeddings.get(i),
                            MIN_SIMILARITY,
                            MAX_SIMILAR_QUESTIONS
                    );

            if (similarQuestions.isEmpty()) {
                continue;
            }

            List<String> similarTexts = similarQuestions.stream()
                    .map(QuestionPool::getQuestion)
                    .toList();

            comparisons.add(new ComparisonQuestion(
                    i,
                    questions.get(i).question(),
                    similarTexts
            ));
        }

        return comparisons;
    }

    // 04. 중복 판정 | LLM으로 기존 질문과 중복되는 새 질문의 인덱스 반환
    private Set<Integer> compareQuestions(List<ComparisonQuestion> comparisons) {
        if (comparisons.isEmpty()) {
            return Set.of();
        }

        return questionClient.compare(comparisons);
    }
}
