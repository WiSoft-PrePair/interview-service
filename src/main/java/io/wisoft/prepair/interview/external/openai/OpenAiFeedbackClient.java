package io.wisoft.prepair.interview.external.openai;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.openai.prompt.FeedbackPrompt;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OpenAiFeedbackClient {

    private final ChatClient chatClient;
    private final FeedbackPrompt feedbackPrompt;

    public OpenAiFeedbackClient(ChatClient.Builder builder, FeedbackPrompt feedbackPrompt) {
        this.chatClient = builder.build();
        this.feedbackPrompt = feedbackPrompt;
    }

    public GeneratedFeedback generate(QuestionTag tag, String question, String answer) {
        GeneratedFeedback feedback = chatClient.prompt()
                .system(feedbackPrompt.system())
                .user(feedbackPrompt.user(tag, question, answer))
                .call()
                .entity(GeneratedFeedback.class);

        validate(feedback);
        return strip(feedback);
    }

    // 평가 결과 검증 | 점수 범위와 피드백 문구가 모두 있는지 확인
    private void validate(GeneratedFeedback feedback) {
        boolean invalid = feedback == null
                || feedback.score() == null
                || feedback.score() < 0 || feedback.score() > 100
                || !StringUtils.hasText(feedback.good())
                || !StringUtils.hasText(feedback.improvement())
                || !StringUtils.hasText(feedback.example())
                || !StringUtils.hasText(feedback.summary());

        if (invalid) {
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

    // 평가 근거를 먼저 작성한 뒤 점수를 매기도록 score를 마지막에 둠
    public record GeneratedFeedback(
            String good,
            String improvement,
            String example,
            String summary,
            Integer score
    ) {
    }
}
