package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuestionReader {

    private final QuestionRepository questionRepository;

    public boolean isAssigned(UUID memberId, LocalDate today) {
        return questionRepository.isAssigned(memberId, today);
    }
}
