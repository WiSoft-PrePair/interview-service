package io.wisoft.prepair.interview.question.legacy.service;

import io.wisoft.prepair.interview.external.member.dto.MemberProfile;
import io.wisoft.prepair.interview.external.member.enums.Notification;
import io.wisoft.prepair.interview.external.notification.email.EmailService;
import io.wisoft.prepair.interview.external.notification.kakao.KakaoService;
import io.wisoft.prepair.interview.question.legacy.entity.InterviewQuestion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionNotificationService {

    private final EmailService emailService;
    private final KakaoService kakaoService;

    public void notifyMember(MemberProfile member, InterviewQuestion question) {
        Notification notification = member.notification();

        if (notification == Notification.EMAIL || notification == Notification.BOTH) {
            sendEmail(member, question);
        }

        if (notification == Notification.KAKAO || notification == Notification.BOTH) {
            sendKakao(member, question);
        }
    }

    private void sendEmail(MemberProfile member, InterviewQuestion question) {
        try {
            emailService.send(
                    member.email(),
                    member.nickname(),
                    question.getQuestionTag(),
                    question.getQuestion()
            );
            log.info("이메일 발송 완료 | memberId={}", member.id());
        } catch (Exception e) {
            log.error("이메일 발송 실패 | memberId={}", member.id(), e);
        }
    }

    private void sendKakao(MemberProfile member, InterviewQuestion question) {
        if (member.kakaoAccessToken() == null || member.kakaoAccessToken().isBlank()) {
            log.warn("멤버 스킵 | memberId={} | reason=no_kakao_token", member.id());
            return;
        }

        try {
            kakaoService.send(
                    member.kakaoAccessToken(),
                    question.getQuestion(),
                    question.getQuestionTag()
            );
            log.info("카카오 발송 완료 | memberId={}", member.id());
        } catch (HttpClientErrorException.Unauthorized e) {
            log.warn("카카오 발송 실패 | memberId={} | reason=token_expired", member.id());
        } catch (Exception e) {
            log.error("카카오 발송 실패 | memberId={}", member.id(), e);
        }
    }
}
