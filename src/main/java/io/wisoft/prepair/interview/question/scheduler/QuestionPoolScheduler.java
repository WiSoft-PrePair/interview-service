package io.wisoft.prepair.interview.question.scheduler;

import io.wisoft.prepair.interview.question.service.QuestionPoolGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionPoolScheduler {

    private static final int QUESTION_PER_JOB = 5;

    private final QuestionPoolGenerator poolGenerator;

    //    @Scheduled(cron = "0 0 6,7 * * *", zone = "Asia/Seoul")
    @SchedulerLock(name = "prepareQuestionPool", lockAtMostFor = "50m")
    public void prepare() {
        log.debug("질문 풀 생성 스케줄러 시작");
        poolGenerator.execute(QUESTION_PER_JOB);
    }
}
