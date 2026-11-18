package io.wisoft.prepair.interview.external.openai.client;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.openai.dto.ComparisonQuestion;
import io.wisoft.prepair.interview.external.openai.dto.DuplicateIndexList;
import io.wisoft.prepair.interview.external.openai.dto.GeneratedQuestion;
import io.wisoft.prepair.interview.external.openai.dto.GeneratedQuestionList;
import io.wisoft.prepair.interview.external.openai.prompt.QuestionPrompt;
import io.wisoft.prepair.interview.external.openai.support.CallType;
import io.wisoft.prepair.interview.external.openai.support.OpenAiChat;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
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

    public List<GeneratedQuestion> generate(String job, QuestionTag tag, int count) {
        GeneratedQuestionList response = openAiChat.call(
                CallType.QUESTION,
                questionPrompt.generateSystem(),
                questionPrompt.generateUser(job, tag, count),
                new ParameterizedTypeReference<>() {
                });

        return validate(response.questions());
    }

    public Set<Integer> compare(List<ComparisonQuestion> comparisons) {
        DuplicateIndexList response = openAiChat.call(
                CallType.COMPARE,
                questionPrompt.compareSystem(),
                questionPrompt.compareUser(comparisons),
                new ParameterizedTypeReference<>() {
                });

        return validate(comparisons, Set.copyOf((response.indexes())));
    }

    private List<GeneratedQuestion> validate(List<GeneratedQuestion> questions) {
        List<GeneratedQuestion> validQuestions = questions.stream()
                .filter(question -> StringUtils.hasText(question.question()))
                .map(question -> new GeneratedQuestion(question.question().strip()))
                .toList();

        if (validQuestions.size() < questions.size()) {
            log.warn("OpenAI 질문 생성 응답 중 {}개 제외", questions.size() - validQuestions.size());
        }

        if (validQuestions.isEmpty()) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

        return validQuestions;
    }

    private Set<Integer> validate(List<ComparisonQuestion> comparisons, Set<Integer> duplicateIndexes) {
        Set<Integer> requestedIndexes = comparisons.stream()
                .map(ComparisonQuestion::index)
                .collect(Collectors.toSet());

        boolean invalid = duplicateIndexes.stream()
                .anyMatch(index -> !requestedIndexes.contains(index));

        if (invalid) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

        return duplicateIndexes;
    }
}
