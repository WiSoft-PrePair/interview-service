package io.wisoft.prepair.interview.question.controller;

import io.wisoft.prepair.interview.question.scheduler.QuestionPoolScheduler;
import io.wisoft.prepair.interview.question.scheduler.QuestionScheduler;
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
@RequestMapping("/test")
public class QuestionTestController {

    private final QuestionPoolScheduler poolScheduler;
    private final QuestionScheduler questionScheduler;

    @PostMapping("/question-pool")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void prepare() {
        poolScheduler.prepare();
    }

    @PostMapping("/question-assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assign() {
        questionScheduler.assign();
    }
}
