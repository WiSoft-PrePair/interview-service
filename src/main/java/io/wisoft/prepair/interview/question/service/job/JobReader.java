package io.wisoft.prepair.interview.question.service.job;


import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobReader {

    private final JobRepository jobRepository;

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Job findByName(String input) {
        return jobRepository.findByName(input).orElse(null);
    }

    public Optional<Job> findSimilarJob(float[] embedding, double minSimilarity) {
        return jobRepository.findSimilarJob(embedding, minSimilarity);
    }
}
