package io.wisoft.prepair.interview.legacy.session.notifier;

import io.wisoft.prepair.interview.common.support.SseEmitterManager;
import io.wisoft.prepair.interview.legacy.answer.dto.response.FinalFeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionCompletionNotifier {

    private static final String FINAL_COMPLETE_EVENT = "final-complete";
    private static final String ANALYSIS_FAILED_EVENT = "analysis-failed";

    private final SseEmitterManager sseEmitterManager;

    public void notifyComplete(UUID sessionId, FinalFeedbackResponse response) {
        sseEmitterManager.send(sessionId, FINAL_COMPLETE_EVENT, response);
        sseEmitterManager.complete(sessionId);
    }

    public void notifyFailure(UUID sessionId, String message) {
        sseEmitterManager.send(sessionId, ANALYSIS_FAILED_EVENT, Map.of("message", message));
        sseEmitterManager.complete(sessionId);
    }
}
