package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.external.openai.client.OpenAiEmbeddingClient;
import io.wisoft.prepair.interview.external.openai.client.OpenAiQuestionClient;
import io.wisoft.prepair.interview.external.openai.dto.GeneratedQuestion;
import io.wisoft.prepair.interview.external.openai.dto.ComparisonQuestion;
import io.wisoft.prepair.interview.question.entity.enums.Job;
import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionPoolGenerator {

    private static final double CANDIDATE_MIN_SIMILARITY = 0.4;
    private static final int CANDIDATE_LIMIT = 5;

    private final OpenAiQuestionClient questionClient;
    private final OpenAiEmbeddingClient embeddingClient;
    private final QuestionPoolReader poolReader;
    private final QuestionPoolWriter poolWriter;

    // 백그라운드 생성 | job이 없으면 전체 직무, 직무마다 분류별로 countPerTag개씩. 실패한 조합은 로그만 남기고 다음으로
    @Async("questionPoolExecutor")
    public void execute(Job job, int countPerTag) {
        long startedAt = System.nanoTime();
        List<Job> jobs = job == null ? List.of(Job.values()) : List.of(job);

        for (Job target : jobs) {
            for (QuestionTag tag : QuestionTag.values()) {
                try {
                    generate(target, tag, countPerTag);
                } catch (Exception e) {
                    log.error("질문 풀 생성 실패: job={}, tag={}", target, tag, e);
                }
            }
        }

        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
        log.info("질문 풀 생성 완료: jobs={}, countPerTag={}, durationMs={}", jobs.size(), countPerTag, durationMs);
    }

    // 직무·분류 하나 생성 | 질문 생성 → 각 질문 임베딩(01) → 유사한 기존 질문 조회(02) → LLM 중복 판정(03) → 중복 제외 후 저장
    private void generate(Job job, QuestionTag tag, int count) {
        List<GeneratedQuestion> questions = questionClient.generate(
                job.getTitle(),
                tag,
                count
        );

        List<float[]> embeddings = embedQuestions(questions);

        List<ComparisonQuestion> comparisons = getComparisons(job, questions, embeddings);
        Set<Integer> duplicateIndexes = compareQuestions(comparisons);

        poolWriter.save(job, tag, questions, embeddings, duplicateIndexes);
    }

    // 01. 임베딩 생성 | 새 질문들을 한 번에 벡터로 변환
    private List<float[]> embedQuestions(List<GeneratedQuestion> questions) {
        List<String> texts = questions.stream()
                .map(GeneratedQuestion::question)
                .toList();

        return embeddingClient.embed(texts);
    }

    // 02. 비교 대상 구성 | 같은 직무에서 유사도 0.4 이상인 기존 질문을 최대 5개 조회
    private List<ComparisonQuestion> getComparisons(Job job, List<GeneratedQuestion> questions, List<float[]> embeddings) {
        List<ComparisonQuestion> comparisons = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            List<QuestionPool> similarQuestions = poolReader
                    .findSimilar(
                            job,
                            embeddings.get(i),
                            CANDIDATE_MIN_SIMILARITY,
                            CANDIDATE_LIMIT
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

    // 03. 중복 판정 | LLM으로 기존 질문과 중복되는 새 질문의 인덱스 반환
    private Set<Integer> compareQuestions(List<ComparisonQuestion> comparisons) {
        if (comparisons.isEmpty()) {
            return Set.of();
        }

        return questionClient.compare(comparisons);
    }
}
