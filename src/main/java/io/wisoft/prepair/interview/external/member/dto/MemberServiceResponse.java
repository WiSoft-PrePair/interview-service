package io.wisoft.prepair.interview.external.member.dto;

public record MemberServiceResponse<T>(
        int statusCode,
        T data,
        String message
) {
}
