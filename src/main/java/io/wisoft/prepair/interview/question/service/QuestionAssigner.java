package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.member.MemberServiceClient;
import io.wisoft.prepair.interview.external.member.dto.MemberProfile;
import io.wisoft.prepair.interview.question.dto.response.QuestionDetail;
import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.entity.enums.Job;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuestionAssigner {

    private final MemberServiceClient memberClient;
    private final QuestionReader questionReader;
    private final QuestionWriter questionWriter;
    private final QuestionPoolReader poolReader;

    // 질문 받기 | 한도 확인(01) → 직무 조회(02) → 풀에서 고르기(03) → 순번과 함께 저장
    public QuestionDetail assign(UUID memberId, QuestionTag tag) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int used = checkLimit(memberId, today);
        Job job = getJob(memberId);

        QuestionPool pool = pick(job, tag, memberId);
        Question question = questionWriter.save(memberId, pool, today, used + 1);

        return QuestionDetail.from(question);
    }

    // 01. 한도 확인 | 오늘 받은 개수를 돌려줌
    private int checkLimit(UUID memberId, LocalDate today) {
        int used = questionReader.countToday(memberId, today);

        if (used >= Question.DAILY_LIMIT) {
            throw new BusinessException(ErrorCode.DAILY_LIMIT_EXCEEDED);
        }

        return used;
    }

    // 02. 직무 조회 | member 서비스에서 회원 직무 코드
    private Job getJob(UUID memberId) {
        MemberProfile member = memberClient.getMember(memberId);

        if (member.job() == null || member.job().isBlank()) {
            throw new BusinessException(ErrorCode.JOB_NOT_SET);
        }

        return Job.valueOf(member.job());
    }

    // 03. 풀에서 고르기 | 직무 + 카테고리에서 아직 안 받은 질문 하나
    private QuestionPool pick(Job job, QuestionTag tag, UUID memberId) {
        return poolReader.pickUnassigned(job, tag, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_POOL_EXHAUSTED));
    }
}
