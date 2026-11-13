package io.wisoft.prepair.interview.answer.controller;

import io.wisoft.prepair.interview.answer.dto.request.AnswerRequest;
import io.wisoft.prepair.interview.answer.dto.response.AnswerPreview;
import io.wisoft.prepair.interview.answer.dto.response.AnswerDetail;
import io.wisoft.prepair.interview.answer.service.AnswerService;
import io.wisoft.prepair.interview.answer.service.AnswerSubmitter;
import io.wisoft.prepair.interview.common.response.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews")
public class AnswerController {

    private final AnswerService answerService;
    private final AnswerSubmitter answerSubmitter;

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<ApiResponse<AnswerDetail>> submitAnswer(
            @RequestHeader("X-User-Id") UUID memberId,
            @PathVariable UUID questionId,
            @Valid @RequestBody AnswerRequest request
    ) {
        AnswerDetail data = answerSubmitter.submit(questionId, memberId, request.answer());
        ApiResponse<AnswerDetail> response = ApiResponse.of(data, "답변을 제출했습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<ApiResponse<List<AnswerPreview>>> getAnswers(
            @RequestHeader("X-User-Id") UUID memberId,
            @PathVariable UUID questionId
    ) {
        List<AnswerPreview> data = answerService.getAnswers(questionId, memberId);
        ApiResponse<List<AnswerPreview>> response = ApiResponse.of(data, "답변 기록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/answers/{answerId}")
    public ResponseEntity<ApiResponse<AnswerDetail>> getAnswer(
            @RequestHeader("X-User-Id") UUID memberId,
            @PathVariable UUID answerId
    ) {
        AnswerDetail data = answerService.getAnswer(answerId, memberId);
        ApiResponse<AnswerDetail> response = ApiResponse.of(data, "답변 상세를 조회했습니다.");
        return ResponseEntity.ok(response);
    }
}
