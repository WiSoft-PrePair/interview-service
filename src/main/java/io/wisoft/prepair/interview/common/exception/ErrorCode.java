package io.wisoft.prepair.interview.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 요청
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    INVALID_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "잘못된 경로 변수입니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "올바르지 않은 JSON 형식입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),

    // 서버 오류
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // 접근 권한
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 면접 세션
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "면접 세션을 찾을 수 없습니다."),
    SESSION_NOT_FINISHED(HttpStatus.BAD_REQUEST, "아직 종료되지 않은 면접 세션입니다."),

    // 면접 질문
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),

    // 면접 답변
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다."),

    // 외부 연동: 회원 서비스
    MEMBER_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "회원 서비스 요청 처리 중 오류가 발생했습니다."),
    MEMBER_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "회원 서비스에 연결할 수 없습니다."),

    // 외부 연동: OpenAI
    OPENAI_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI API 호출 중 오류가 발생했습니다."),
    OPENAI_RESPONSE_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI 응답을 읽을 수 없습니다."),
    OPENAI_INVALID_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI 응답이 유효하지 않습니다."),

    // 미디어 처리
    STT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "음성을 텍스트로 변환하지 못했습니다."),
    FRAME_EXTRACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "영상 프레임 추출에 실패했습니다."),
    VIDEO_CONVERSION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "영상 변환에 실패했습니다."),

    // 파일 업로드
    VIDEO_FILE_TOO_LARGE(HttpStatus.CONTENT_TOO_LARGE, "파일 크기가 제한을 초과했습니다. (최대 150MB)");

    private final HttpStatus httpStatus;
    private final String message;
}
