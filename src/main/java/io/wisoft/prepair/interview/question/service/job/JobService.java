package io.wisoft.prepair.interview.question.service.job;

import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Transactional
    public Job save(String input, float[] embedding) {
        return jobRepository.save(
                new Job(input, embedding)
        );
    }
}
