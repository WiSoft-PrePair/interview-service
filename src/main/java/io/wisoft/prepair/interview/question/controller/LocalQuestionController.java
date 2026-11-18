package io.wisoft.prepair.interview.question.controller;

import io.wisoft.prepair.interview.question.entity.enums.Job;
import io.wisoft.prepair.interview.question.service.QuestionAssigner;
import io.wisoft.prepair.interview.question.service.QuestionPoolGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Profile("local")
@RestController
@RequiredArgsConstructor
@RequestMapping("/local")
public class LocalQuestionController {

    private final QuestionPoolGenerator poolGenerator;
    private final QuestionAssigner questionAssigner;

    // 직무 하나 생성
    @PostMapping("/question-pool/{job}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void generate(
            @PathVariable Job job,
            @RequestParam int count
    ) {
        log.info("[local] 질문 풀 생성 시작: job={}, count={}", job, count);
        poolGenerator.generate(job, count);
    }

    // 전체 직무 생성
    @PostMapping("/question-pool/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void generateAll(@RequestParam int count) {
        log.info("[local] 전체 직무 질문 풀 생성 시작: jobs={}, countPerJob={}, total={}",
                Job.values().length,
                count,
                Job.values().length * count
        );

        poolGenerator.execute(count);
    }

    @PostMapping("/question-assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assign() {
        log.info("[local] 질문 배정 수동 실행");
        questionAssigner.execute();
    }
}
