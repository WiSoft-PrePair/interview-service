package io.wisoft.prepair.interview.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor videoTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(6);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("video-task");
        return executor;
    }

    // 질문 풀 생성 | 한 번에 하나만 실행, 실행 중이면 새 요청은 거절 (같은 생성이 겹쳐 비용이 두 배로 나가는 것 방지)
    @Bean
    public Executor questionPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("question-pool");
        return executor;
    }
}
