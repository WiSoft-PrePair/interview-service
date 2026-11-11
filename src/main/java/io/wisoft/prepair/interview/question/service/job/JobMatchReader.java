package io.wisoft.prepair.interview.question.service.job;

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
}
