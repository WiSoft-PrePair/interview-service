package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.entity.enums.QuestionStatus;
import io.wisoft.prepair.interview.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class QuestionWriter {

    private final QuestionRepository questionRepository;

    // 동시 요청이 같은 순번이나 같은 풀 질문으로 겹치면 유니크 제약에 걸림 → 충돌로 응답
    public Question save(UUID memberId, QuestionPool pool, LocalDate assignedDate, int dailySeq) {
        try {
            return questionRepository.saveAndFlush(
                    new Question(memberId, pool, assignedDate, dailySeq)
            );
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.QUESTION_ASSIGN_CONFLICT, e);
        }
    }

    public void answered(UUID questionId, Integer score) {
        questionRepository.answered(
                questionId, QuestionStatus.ANSWERED, score, Instant.now()
        );
    }
}
