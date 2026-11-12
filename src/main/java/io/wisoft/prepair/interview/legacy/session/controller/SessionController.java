package io.wisoft.prepair.interview.legacy.session.controller;

import io.wisoft.prepair.interview.common.response.ApiResponse;
import io.wisoft.prepair.interview.legacy.session.dto.response.CreateSessionResponse;
import io.wisoft.prepair.interview.legacy.session.dto.response.SessionDetailResponse;
import io.wisoft.prepair.interview.legacy.session.dto.response.SessionResponse;
import io.wisoft.prepair.interview.legacy.session.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews/sessions")
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessions(
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        List<SessionResponse> data = sessionService.getSessionsByMember(memberId);
        ApiResponse<List<SessionResponse>> response = ApiResponse.of(data, "세션 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<SessionDetailResponse>> getSession(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        SessionDetailResponse data = sessionService.getSessionDetail(sessionId, memberId);
        ApiResponse<SessionDetailResponse> response = ApiResponse.of(data, "세션 결과를 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<CreateSessionResponse>> createSession(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        CreateSessionResponse data = sessionService.createSession(sessionId, memberId);
        ApiResponse<CreateSessionResponse> response = ApiResponse.of(data, "세션이 생성되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{sessionId}/questions/{questionId}")
    public ResponseEntity<ApiResponse<CreateSessionResponse>> createQuestion(
            @PathVariable UUID sessionId,
            @PathVariable UUID questionId,
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        CreateSessionResponse data = sessionService.createQuestion(sessionId, questionId, memberId);
        ApiResponse<CreateSessionResponse> response = ApiResponse.of(data, "세션이 생성되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
