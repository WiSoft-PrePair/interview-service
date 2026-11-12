package io.wisoft.prepair.interview.question.scheduler;


import io.wisoft.prepair.interview.question.service.QuestionAssigner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionScheduler {

    private final QuestionAssigner questionAssigner;

    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
    @SchedulerLock(name = "assignQuestions")
    public void assign() {
        log.debug("질문 배정 스케줄러 시작");
        questionAssigner.execute();
    }
}
