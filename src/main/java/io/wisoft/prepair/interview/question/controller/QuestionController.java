package io.wisoft.prepair.interview.question.controller;


import io.wisoft.prepair.interview.common.response.CursorResponse;
import io.wisoft.prepair.interview.common.response.ApiResponse;
import io.wisoft.prepair.interview.question.dto.request.QuestionCursorRequest;
import io.wisoft.prepair.interview.question.dto.response.QuestionDetail;
import io.wisoft.prepair.interview.question.dto.response.QuestionPreview;
import io.wisoft.prepair.interview.question.dto.response.TagCountResponse;
import io.wisoft.prepair.interview.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews/questions")
public class QuestionController {

    private final QuestionService questionService;

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
