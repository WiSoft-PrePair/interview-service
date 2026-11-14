package io.wisoft.prepair.interview.external.openai.client;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.openai.dto.ComparisonQuestion;
import io.wisoft.prepair.interview.external.openai.dto.GeneratedQuestion;
import io.wisoft.prepair.interview.external.openai.prompt.QuestionPrompt;
import io.wisoft.prepair.interview.external.openai.support.CallType;
import io.wisoft.prepair.interview.external.openai.support.OpenAiChat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiQuestionClient {

    private final OpenAiChat openAiChat;
    private final QuestionPrompt questionPrompt;

    public List<GeneratedQuestion> generate(String job, int count) {
        List<GeneratedQuestion> questions = openAiChat.call(
                CallType.QUESTION,
                questionPrompt.generateSystem(),
                questionPrompt.generateUser(job, count),
                new ParameterizedTypeReference<>() {
                });

        return validate(questions);
    }

    public Set<Integer> compare(List<ComparisonQuestion> comparisons) {
        Set<Integer> duplicateIndexes = openAiChat.call(
                CallType.COMPARE,
                questionPrompt.compareSystem(),
                questionPrompt.compareUser(comparisons),
                new ParameterizedTypeReference<>() {
                });

        return validate(comparisons, duplicateIndexes);
    }

    // 생성 결과 검증 | 문구와 분류가 있는 질문만 반환
    private List<GeneratedQuestion> validate(List<GeneratedQuestion> questions) {
        List<GeneratedQuestion> validQuestions = questions.stream()
                .filter(question -> question != null
                        && StringUtils.hasText(question.question())
                        && question.questionTag() != null)
                .map(question -> new GeneratedQuestion(
                        question.question().strip(),
                        question.questionTag()))
                .toList();

        if (validQuestions.size() < questions.size()) {
            log.warn("OpenAI 질문 생성 응답 중 {}개 제외", questions.size() - validQuestions.size());
        }

        if (validQuestions.isEmpty()) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

        return validQuestions;
    }

    // 판정 결과 검증 | 요청한 질문의 인덱스만 허용
    private Set<Integer> validate(List<ComparisonQuestion> comparisons, Set<Integer> duplicateIndexes) {
        Set<Integer> requestedIndexes = comparisons.stream()
                .map(ComparisonQuestion::index)
                .collect(Collectors.toSet());

        boolean invalid = duplicateIndexes.stream()
                .anyMatch(index -> index == null || !requestedIndexes.contains(index));

        if (invalid) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

        return duplicateIndexes;
    }
}
