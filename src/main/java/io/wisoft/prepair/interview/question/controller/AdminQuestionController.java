package io.wisoft.prepair.interview.question.controller;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.common.response.ApiResponse;
import io.wisoft.prepair.interview.question.entity.enums.Job;
import io.wisoft.prepair.interview.question.service.QuestionPoolGenerator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews/admin")
public class AdminQuestionController {

    private final QuestionPoolGenerator poolGenerator;

    // 질문 풀 생성 | job이 없으면 전체 직무, 분류마다 count개씩. 오래 걸려서 접수만 하고 백그라운드에서 실행
    @PostMapping("/question-pool")
    public ResponseEntity<ApiResponse<Void>> generate(
            @RequestParam(required = false) Job job,
            @RequestParam @Min(1) @Max(10) int count
    ) {
        log.info("[admin] 질문 풀 생성 접수: job={}, countPerTag={}", job == null ? "ALL" : job, count);

        try {
            poolGenerator.execute(job, count);
        } catch (TaskRejectedException e) {
            throw new BusinessException(ErrorCode.QUESTION_POOL_GENERATING, e);
        }

        ApiResponse<Void> response = ApiResponse.of(null, "질문 풀 생성을 시작했습니다.");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
