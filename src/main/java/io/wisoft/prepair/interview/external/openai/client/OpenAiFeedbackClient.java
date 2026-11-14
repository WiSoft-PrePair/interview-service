package io.wisoft.prepair.interview.external.openai.client;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.openai.dto.GeneratedFeedback;
import io.wisoft.prepair.interview.external.openai.prompt.FeedbackPrompt;
import io.wisoft.prepair.interview.external.openai.support.CallType;
import io.wisoft.prepair.interview.external.openai.support.OpenAiChat;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiFeedbackClient {

    private final OpenAiChat openAiChat;
    private final FeedbackPrompt feedbackPrompt;

    public GeneratedFeedback generate(QuestionTag tag, String question, String answer) {
        GeneratedFeedback feedback = openAiChat.call(
                CallType.FEEDBACK,
                feedbackPrompt.system(),
                feedbackPrompt.user(tag, question, answer),
                new ParameterizedTypeReference<>() {
                });

        validate(feedback);
        return strip(feedback);
    }

    // 평가 결과 검증 | 점수 범위와 피드백 문구가 모두 있는지 확인
    private void validate(GeneratedFeedback feedback) {
        boolean invalid = feedback.score() == null
                || feedback.score() < 0 || feedback.score() > 100
                || !StringUtils.hasText(feedback.good())
                || !StringUtils.hasText(feedback.improvement())
                || !StringUtils.hasText(feedback.example())
                || !StringUtils.hasText(feedback.summary());

        if (invalid) {
            log.warn("OpenAI 피드백 응답 검증 실패: {}", feedback);
            throw new BusinessException(ErrorCode.OPENAI_INVALID_RESPONSE);
        }
    }

    private GeneratedFeedback strip(GeneratedFeedback feedback) {
        return new GeneratedFeedback(
                feedback.good().strip(),
                feedback.improvement().strip(),
                feedback.example().strip(),
                feedback.summary().strip(),
                feedback.score()
        );
    }
}
