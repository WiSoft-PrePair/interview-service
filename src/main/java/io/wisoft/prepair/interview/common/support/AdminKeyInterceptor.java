package io.wisoft.prepair.interview.common.support;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

// 관리자 API 보호 | 요청 헤더 X-Admin-Key가 설정한 키와 같을 때만 통과
@Component
public class AdminKeyInterceptor implements HandlerInterceptor {

    private static final String HEADER = "X-Admin-Key";

    private final byte[] adminKey;

    public AdminKeyInterceptor(@Value("${admin.api-key}") String adminKey) {
        this.adminKey = adminKey.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String key = request.getHeader(HEADER);

        // MessageDigest.isEqual: 비교 시간이 값과 무관해 응답 시간으로 키를 추측할 수 없음
        if (key == null || !MessageDigest.isEqual(adminKey, key.getBytes(StandardCharsets.UTF_8))) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return true;
    }
}
