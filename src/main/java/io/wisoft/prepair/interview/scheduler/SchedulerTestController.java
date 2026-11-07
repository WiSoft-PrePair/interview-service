package io.wisoft.prepair.interview.scheduler;

import io.wisoft.prepair.interview.question.service.TodayQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("local")
@RequiredArgsConstructor
@RequestMapping("/test")
public class SchedulerTestController {

    private final TodayQuestionService todayQuestionService;

    @PostMapping("/scheduler/today-question")
    public void triggerTodayQuestion() {
        todayQuestionService.sendTodayQuestions();
    }
}
