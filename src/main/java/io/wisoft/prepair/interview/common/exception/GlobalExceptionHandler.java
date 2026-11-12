package io.wisoft.prepair.interview.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException e,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = e.getErrorCode();

        if (errorCode.getHttpStatus().is5xxServerError()) {
            log.error(
                    "비즈니스 처리 실패: code={}, method={}, uri={}",
                    errorCode.name(),
                    request.getMethod(),
                    request.getRequestURI(),
                    e
            );
        } else {
            log.debug(
                    "비즈니스 요청 거부: code={}, status={}, method={}, uri={}",
                    errorCode.name(),
                    errorCode.getHttpStatus().value(),
                    request.getMethod(),
                    request.getRequestURI()
            );
        }

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
        log.debug("존재하지 않는 경로 요청: path={}", e.getResourcePath());
        return ResponseEntity
                .status(ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        List<ErrorDetail> details = e.getAllErrors().stream()
                .map(error -> new ErrorDetail(
                        error instanceof FieldError fieldError ? fieldError.getField() : null,
                        error.getDefaultMessage()))
                .toList();
        log.debug("요청 파라미터 검증 실패: errorCount={}", details.size());

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, details));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorDetail> details = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail(
                        error.getField(),
                        error.isBindingFailure() ? "올바르지 않은 값입니다." : error.getDefaultMessage()))
                .toList();
        log.debug("요청 본문 검증 실패: errorCount={}", details.size());

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, details));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.debug("요청 값 타입 불일치: parameter={}", e.getName());
        return ResponseEntity
                .status(ErrorCode.INVALID_PATH_VARIABLE.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_PATH_VARIABLE));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeader(MissingRequestHeaderException e) {
        log.debug("필수 헤더 누락: header={}", e.getHeaderName());
        return ResponseEntity
                .status(ErrorCode.MISSING_REQUEST_HEADER.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.MISSING_REQUEST_HEADER));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.debug("JSON 요청 본문 파싱 실패");
        return ResponseEntity
                .status(ErrorCode.INVALID_JSON_FORMAT.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_JSON_FORMAT));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        log.debug("지원하지 않는 HTTP 메서드: method={}", e.getMethod());
        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        log.debug("업로드 크기 제한 초과");
        return ResponseEntity
                .status(ErrorCode.VIDEO_FILE_TOO_LARGE.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.VIDEO_FILE_TOO_LARGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        log.error(
                "처리되지 않은 서버 예외: method={}, uri={}",
                request.getMethod(),
                request.getRequestURI(),
                e
        );
        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_ERROR));
    }
}
