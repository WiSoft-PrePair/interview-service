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
public class QuestionAssignScheduler {

    private final QuestionAssigner questionAssigner;

    // 질문 풀 시드 구성 전까지 비활성 (켤 때: 매일 8시 배정)
//    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
    @SchedulerLock(name = "assignQuestions")
    public void assign() {
        log.debug("질문 배정 스케줄러 시작");
        questionAssigner.execute();
    }
}
