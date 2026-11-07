package io.wisoft.prepair.interview.external.member.dto;

import io.wisoft.prepair.interview.external.member.enums.Frequency;
import io.wisoft.prepair.interview.external.member.enums.Notification;

import java.util.List;
import java.util.UUID;

public record MemberSchedulerInfo(
        UUID id,
        String email,
        String job,
        String nickname,
        Notification notification,
        Frequency frequency,
        Integer point,
        Boolean isPro,
        List<String> activity,
        String kakaoAccessToken
) {
}
