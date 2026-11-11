package io.wisoft.prepair.interview.question.service.job;

import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.entity.JobMatch;
import io.wisoft.prepair.interview.question.repository.JobMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobMatchService {

    private final JobMatchRepository jobMatchRepository;

    @Transactional
    public void save(UUID memberId, Job job, String input) {
        jobMatchRepository.save(
                new JobMatch(memberId, job, input)
        );
    }
}
