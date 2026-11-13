package io.wisoft.prepair.interview.answer.dto.response;

import io.wisoft.prepair.interview.answer.entity.Answer;

import java.time.Instant;
import java.util.UUID;

public record AnswerDetail(
        UUID id,
        String answer,
        Feedback feedback,
        Instant createdAt
) {
    public static AnswerDetail from(Answer answer) {
        return new AnswerDetail(
                answer.getId(),
                answer.getAnswer(),
                Feedback.from(answer),
                answer.getCreatedAt()
        );
    }

    public record Feedback(
            Integer score,
            String summary,
            String good,
            String improvement,
            String example
    ) {
        public static Feedback from(Answer answer) {
            return new Feedback(
                    answer.getScore(),
                    answer.getSummary(),
                    answer.getGood(),
                    answer.getImprovement(),
                    answer.getExample()
            );
        }
    }

}
