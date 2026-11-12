package io.wisoft.prepair.interview.legacy.session.dto.response;

import java.util.List;
import java.util.UUID;

public record CreateSessionResponse(UUID sessionId, List<UUID> questionIds) {
}
