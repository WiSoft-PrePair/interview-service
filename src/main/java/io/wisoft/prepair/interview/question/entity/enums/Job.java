package io.wisoft.prepair.interview.question.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {

    BACKEND("백엔드 개발자"),
    FRONTEND("프론트엔드 개발자"),
    FULLSTACK("풀스택 개발자"),
    ANDROID("Android 개발자"),
    IOS("iOS 개발자"),
    CROSS_PLATFORM("크로스플랫폼 앱 개발자"),
    GAME_CLIENT("게임 클라이언트 개발자"),
    GAME_SERVER("게임 서버 개발자"),
    AI_ML("AI/ML 엔지니어"),
    DATA_ENGINEER("데이터 엔지니어"),
    DEVOPS("DevOps 엔지니어"),
    SECURITY("보안 엔지니어"),
    DBA("DBA"),
    EMBEDDED("임베디드 개발자"),
    QA("QA 엔지니어");

    private final String title;
}