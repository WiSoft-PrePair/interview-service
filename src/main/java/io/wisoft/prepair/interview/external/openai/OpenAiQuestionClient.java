package io.wisoft.prepair.interview.external.openai;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.openai.prompt.QuestionPrompt;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OpenAiQuestionClient {

    private final ChatClient chatClient;
    private final QuestionPrompt questionPrompt;

    public OpenAiQuestionClient(ChatClient.Builder builder, QuestionPrompt questionPrompt) {
        this.chatClient = builder.build();
        this.questionPrompt = questionPrompt;
    }

    public List<GeneratedQuestion> generate(String job, int count) {
        List<GeneratedQuestion> questions = call(questionPrompt.generateSystem(),
                questionPrompt.generateUser(job, count),
                new ParameterizedTypeReference<>() {
                });

        return validate(questions);
    }

    public Set<Integer> compare(List<ComparisonQuestion> comparisons) {
        Set<Integer> duplicateIndexes = call(
                questionPrompt.compareSystem(), questionPrompt.compareUser(comparisons),
                new ParameterizedTypeReference<>() {
                });

        return validate(comparisons, duplicateIndexes);
    }

    private <T> T call(String system, String user, ParameterizedTypeReference<T> type) {
        return chatClient.prompt()
                .system(system)
                .user(user)
                .call()
                .entity(type);
    }

    // 생성 결과 검증 | 문구와 분류가 있는 질문만 반환
    private List<GeneratedQuestion> validate(List<GeneratedQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

        List<GeneratedQuestion> validQuestions = questions.stream()
                .filter(question -> question != null
                        && question.question() != null
                        && !question.question().isBlank()
                        && question.questionTag() != null)
                .map(question -> new GeneratedQuestion(
                        question.question().strip(),
                        question.questionTag()))
                .toList();

        if (validQuestions.isEmpty()) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

        return validQuestions;
    }

    // 판정 결과 검증 | 요청한 질문의 인덱스만 허용
    private Set<Integer> validate(List<ComparisonQuestion> comparisons, Set<Integer> duplicateIndexes) {
        if (duplicateIndexes == null) {
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }

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

    public record GeneratedQuestion(
            String question,
            QuestionTag questionTag
    ) {
    }

    public record ComparisonQuestion(
            int index,
            String question,
            List<String> similarQuestions
    ) {
    }
}
