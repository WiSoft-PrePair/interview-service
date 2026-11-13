package io.wisoft.prepair.interview.answer.controller;

import io.wisoft.prepair.interview.answer.dto.request.AnswerRequest;
import io.wisoft.prepair.interview.legacy.answer.dto.response.FeedbackResponse;
import io.wisoft.prepair.interview.common.response.ApiResponse;
import io.wisoft.prepair.interview.legacy.answer.service.LegacyAnswerService;
import io.wisoft.prepair.interview.legacy.answer.service.VideoAnswerSseService;
import jakarta.validation.Valid;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews/questions")
public class AnswerController {

    private final LegacyAnswerService answerService;
    private final VideoAnswerSseService videoAnswerStreamService;

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<ApiResponse<FeedbackResponse>> submitAnswer(
            @PathVariable UUID questionId,
            @RequestHeader("X-User-Id") UUID memberId,
            @RequestBody @Valid AnswerRequest request
    ) {
        FeedbackResponse data = answerService.submitAnswer(questionId, memberId, request.answer());
        ApiResponse<FeedbackResponse> response = ApiResponse.of(data, "답변이 제출되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/{questionId}/video-answers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> submitVideoAnswer(
            @PathVariable UUID questionId,
            @RequestHeader("X-User-Id") UUID memberId,
            @RequestPart("video") MultipartFile video
    ) {
        answerService.submitVideoAnswer(questionId, memberId, video);
        ApiResponse<Void> response = ApiResponse.of(null, "영상 답변이 제출되었습니다.");
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping(value = "/video-answers/{sessionId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeSession(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        return videoAnswerStreamService.subscribe(sessionId, memberId);
    }
}
