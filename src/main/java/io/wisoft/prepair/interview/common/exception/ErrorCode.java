package io.wisoft.prepair.interview.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    INVALID_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "잘못된 경로 변수입니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "올바르지 않은 JSON 형식입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // Auth
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // Session
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "세션을 찾을 수 없습니다."),
    SESSION_NOT_FINISHED(HttpStatus.BAD_REQUEST, "아직 진행 중인 세션입니다."),

    // Question
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),

    // Answer
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다."),

    // Member Service
    MEMBER_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Member 서비스에서 오류가 발생했습니다."),
    MEMBER_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Member 서비스에 연결할 수 없습니다."),

    // OpenAI
    OPENAI_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI API 호출 중 오류가 발생했습니다."),
    OPENAI_RESPONSE_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI 응답 파싱에 실패했습니다."),
    OPENAI_INVALID_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI 응답이 유효하지 않습니다."),
    STT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "음성 텍스트 변환에 실패했습니다."),

    // Video Analysis
    FRAME_EXTRACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "영상 프레임 추출에 실패했습니다."),

    // Storage
    VIDEO_CONVERSION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "영상 변환에 실패했습니다."),
    VIDEO_FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "파일 크기가 제한을 초과했습니다. (최대 150MB)");

    private final HttpStatus httpStatus;
    private final String message;
}
