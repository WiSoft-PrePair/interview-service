package io.wisoft.prepair.interview.question.controller;


import io.wisoft.prepair.interview.common.response.CursorResponse;
import io.wisoft.prepair.interview.common.response.ApiResponse;
import io.wisoft.prepair.interview.question.dto.request.QuestionAssignRequest;
import io.wisoft.prepair.interview.question.dto.request.QuestionCursorRequest;
import io.wisoft.prepair.interview.question.dto.response.QuestionDetail;
import io.wisoft.prepair.interview.question.dto.response.QuestionPreview;
import io.wisoft.prepair.interview.question.dto.response.QuestionQuota;
import io.wisoft.prepair.interview.question.dto.response.TagCountResponse;
import io.wisoft.prepair.interview.question.service.QuestionAssigner;
import io.wisoft.prepair.interview.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionAssigner questionAssigner;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionDetail>> assignQuestion(
            @RequestHeader("X-User-Id") UUID memberId,
            @Valid @RequestBody QuestionAssignRequest request
    ) {
        QuestionDetail data = questionAssigner.assign(memberId, request.tag());
        ApiResponse<QuestionDetail> response = ApiResponse.of(data, "면접 질문을 받았습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/quota")
    public ResponseEntity<ApiResponse<QuestionQuota>> getQuota(
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        QuestionQuota data = questionService.getQuota(memberId);
        ApiResponse<QuestionQuota> response = ApiResponse.of(data, "오늘 남은 횟수를 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CursorResponse<QuestionPreview>>> getQuestions(
            @RequestHeader("X-User-Id") UUID memberId,
            @Valid @ModelAttribute QuestionCursorRequest request
    ) {
        CursorResponse<QuestionPreview> data = questionService.getQuestions(memberId, request);
        ApiResponse<CursorResponse<QuestionPreview>> response = ApiResponse.of(data, "질문 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionDetail>> getQuestion(
            @RequestHeader("X-User-Id") UUID memberId,
            @PathVariable UUID questionId
    ) {
        QuestionDetail data = questionService.getQuestion(questionId, memberId);
        ApiResponse<QuestionDetail> response = ApiResponse.of(data, "질문 상세를 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tag-counts")
    public ResponseEntity<ApiResponse<TagCountResponse>> getTagCounts(
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        TagCountResponse data = questionService.getTagCounts(memberId);
        ApiResponse<TagCountResponse> response = ApiResponse.of(data, "태그별 질문 수를 조회했습니다.");
        return ResponseEntity.ok(response);
    }
}