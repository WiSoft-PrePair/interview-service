package io.wisoft.prepair.interview.question.controller;

import io.wisoft.prepair.interview.question.scheduler.QuestionPoolScheduler;
import io.wisoft.prepair.interview.question.service.job.JobMatcher;
import io.wisoft.prepair.interview.question.service.QuestionPoolGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Profile("local")
@RestController
@RequiredArgsConstructor
@RequestMapping("/local/question-pool")
public class QuestionPoolTestController {

    private final QuestionPoolScheduler poolScheduler;

    private final QuestionPoolGenerator poolGenerator;
    private final JobMatcher jobMatcher;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void prepare() {
        poolScheduler.prepare();
    }
}
