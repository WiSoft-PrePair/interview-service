package io.wisoft.prepair.interview.external.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

// Member 서비스 응답에 여기 없는 필드가 있어도 무시한다 (예: kakaoAccessToken)
@JsonIgnoreProperties(ignoreUnknown = true)
public record MemberProfile(
        UUID id,
        String email,
        String job,
        String nickname,
        Integer point,
        Boolean isPro,
        List<String> activity
) {
}
