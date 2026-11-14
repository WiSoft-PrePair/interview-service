package io.wisoft.prepair.interview.question.service.job;

import io.wisoft.prepair.interview.external.member.MemberServiceClient;
import io.wisoft.prepair.interview.external.member.dto.MemberProfile;
import io.wisoft.prepair.interview.external.openai.client.OpenAiEmbeddingClient;
import io.wisoft.prepair.interview.question.entity.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobMatcher {

    // 기준 직무 매칭에 사용할 최소 유사도
    private static final double MIN_SIMILARITY = 0.8;

    private final OpenAiEmbeddingClient embeddingClient;
    private final MemberServiceClient memberClient;

    private final JobMatchReader jobMatchReader;
    private final JobMatchService jobMatchService;

    private final JobReader jobReader;
    private final JobService jobService;

    public void execute() {
        for (MemberProfile member : getMembers()) {
            try {
                match(member.id(), member.job().strip());
            } catch (Exception e) {
                log.error("직무 매핑 실패: memberId={}", member.id(), e);
            }
        }
    }

    private void match(UUID memberId, String input) {
        // 1. 입력이 그대로면 기존 매핑을 유지한다.
        if (jobMatchReader.hasSameInput(memberId, input)) return;

        Job job = jobReader.findByName(input);

        // 2. 이름과 유사도(0.8)로 기준 직무를 찾고, 없으면 생성한다.
        if (job == null) {
            float[] embedding = embeddingClient.embed(input);

            job = jobReader.findSimilarJob(embedding, MIN_SIMILARITY)
                    .orElseGet(() -> jobService.save(input, embedding));
        }

        // 3. 회원과 기준 직무의 매핑을 저장한다.
        jobMatchService.save(memberId, job, input);
    }

    private List<MemberProfile> getMembers() {
        return memberClient.getMembers().stream()
                .filter(member -> member.job() != null && !member.job().isBlank())
                .toList();
    }
}
