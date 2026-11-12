package io.wisoft.prepair.interview.question.legacy.controller;


import io.wisoft.prepair.interview.question.legacy.dto.VideoInterviewRequest;
import io.wisoft.prepair.interview.question.legacy.dto.QuestionResponse;
import io.wisoft.prepair.interview.question.legacy.entity.enums.QuestionType;
import io.wisoft.prepair.interview.common.response.ApiResponse;
import io.wisoft.prepair.interview.question.legacy.service.LegacyQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews/questions")
public class QuestionController {

    private final LegacyQuestionService questionService;

    @GetMapping
    public ApiResponse<List<QuestionResponse>> getQuestions(
            @RequestHeader("X-User-Id") UUID memberId,
            @RequestParam QuestionType type
    ) {
        List<QuestionResponse> data = questionService.getQuestions(memberId, type);
        return ApiResponse.ok(data, "질문 목록을 조회했습니다.");
    }

    @GetMapping("/{questionId}")
    public ApiResponse<QuestionResponse> getQuestion(
            @PathVariable UUID questionId,
            @RequestHeader("X-User-Id") UUID memberId
    ) {
        QuestionResponse data = questionService.getQuestion(questionId, memberId);
        return ApiResponse.ok(data, "질문 단건을 조회했습니다.");
    }

    @PostMapping("/video")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<QuestionResponse>> generateVideoQuestion(
            @RequestHeader("X-User-Id") UUID memberId,
            @RequestBody @Valid VideoInterviewRequest request
    ) {
        List<QuestionResponse> data = questionService.generateVideoQuestions(memberId, request);
        return ApiResponse.created(data, "화상 면접 질문이 생성되었습니다.");
    }
}
