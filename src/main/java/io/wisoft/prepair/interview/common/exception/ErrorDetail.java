package io.wisoft.prepair.interview.common.exception;

public record ErrorDetail(
        String field,
        String message
) {
}
