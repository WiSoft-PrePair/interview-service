package io.wisoft.prepair.interview.common.exception;

public record ErrorResponse(
        int statusCode,
        ErrorDetail error
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus().value(),
                new ErrorDetail(errorCode.name(), errorCode.getMessage()));
    }
}
