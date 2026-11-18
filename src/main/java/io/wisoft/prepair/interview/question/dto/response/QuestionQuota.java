package io.wisoft.prepair.interview.question.dto.response;

public record QuestionQuota(
        int used,
        int limit,
        int remaining
) {
    public static QuestionQuota of(int used, int limit, int remaining) {
        return new QuestionQuota(used, limit, remaining);
    }
}
