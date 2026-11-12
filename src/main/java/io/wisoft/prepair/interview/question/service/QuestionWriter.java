package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class QuestionWriter {

    private final QuestionRepository questionRepository;

    public void save(UUID memberId, QuestionPool pool, LocalDate assignedDate) {
        questionRepository.save(
                new Question(memberId, pool, assignedDate)
        );
    }
}
