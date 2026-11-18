package io.wisoft.prepair.interview.common.config;

import io.wisoft.prepair.interview.common.support.AdminKeyInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AdminKeyInterceptor adminKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminKeyInterceptor)
                .addPathPatterns("/api/interviews/admin/**");
    }
}
