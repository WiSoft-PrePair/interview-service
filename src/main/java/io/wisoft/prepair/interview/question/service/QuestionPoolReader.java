package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.entity.enums.Job;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
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

    public List<QuestionPool> findSimilar(Job job, float[] embedding, double minSimilarity, int candidateLimit) {
        return poolRepository.findSimilar(job, embedding, minSimilarity, candidateLimit);
    }

    public Optional<QuestionPool> pickUnassigned(Job job, QuestionTag tag, UUID memberId) {
        return poolRepository.pickUnassigned(job, tag, memberId);
    }
}


