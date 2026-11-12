package io.wisoft.prepair.interview.question.service.job;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.repository.JobMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobMatchReader {

    private final JobMatchRepository jobMatchRepository;

    public boolean hasSameInput(UUID memberId, String input) {
        return jobMatchRepository.findById(memberId)
                .filter(jobMatch -> jobMatch.getInput().equals(input))
                .isPresent();
    }

    public Job getJob(UUID memberId) {
        return jobMatchRepository.getJob(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_MATCH_NOT_FOUND));
    }
}
