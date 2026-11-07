package io.wisoft.prepair.interview.answer.event;

import java.util.UUID;

public record AnalysisCompletedEvent(UUID answerId, boolean hasFailed) {
}
