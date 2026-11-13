package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.external.member.MemberServiceClient;
import io.wisoft.prepair.interview.external.member.dto.MemberProfile;
import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.service.job.JobMatchReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionAssigner {

    private final MemberServiceClient memberClient;
    private final QuestionWriter questionWriter;
    private final QuestionReader questionReader;
    private final JobMatchReader jobMatchReader;
    private final QuestionPoolReader poolReader;

    public void execute() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        for (MemberProfile member : getMembers()) {
            try {
                assign(member.id(), today);
            } catch (Exception e) {
                log.error("질문 배정 실패: memberId={}", member.id(), e);
            }
        }
    }

    // 회원별 질문 배정 · 같은 날짜에는 한 번만 배정
    private void assign(UUID memberId, LocalDate today) {
        if (questionReader.isAssigned(memberId, today)) return;

        Job job = jobMatchReader.getJob(memberId);

        poolReader.pickUnassigned(job.getId(), memberId)
                .ifPresentOrElse(
                        pool -> questionWriter.save(memberId, pool, today),
                        () -> log.warn("질문 배정 후보 없음: memberId={}, jobId={}", memberId, job.getId())
                );
    }

    private List<MemberProfile> getMembers() {
        return memberClient.getMembers().stream()
                .filter(member -> member.job() != null && !member.job().isBlank())
                .toList();
    }
}

