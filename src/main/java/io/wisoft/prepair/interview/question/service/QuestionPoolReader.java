package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.repository.QuestionPoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuestionPoolReader {

    private final QuestionPoolRepository poolRepository;

    public Integer getMinRemaining(UUID jobId) {
        return poolRepository.getMinRemaining(jobId);
    }

    public List<QuestionPool> findSimilar(UUID jobId, float[] embedding, double minSimilarity, int candidateLimit) {
        return poolRepository.findSimilar(jobId, embedding, minSimilarity, candidateLimit);
    }

    public Optional<QuestionPool> pickUnassigned(UUID jobId, UUID memberId) {
        return poolRepository.pickUnassigned(jobId, memberId);
    }
}


