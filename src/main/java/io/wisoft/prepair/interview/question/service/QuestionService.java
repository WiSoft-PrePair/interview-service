package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void save(UUID memberId, QuestionPool pool, LocalDate assignedDate) {
        questionRepository.save(
                new Question(memberId, pool, assignedDate)
        );
    }
}
