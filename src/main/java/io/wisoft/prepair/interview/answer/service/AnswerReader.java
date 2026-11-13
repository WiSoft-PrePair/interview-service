package io.wisoft.prepair.interview.answer.service;

import io.wisoft.prepair.interview.answer.entity.Answer;
import io.wisoft.prepair.interview.answer.repository.AnswerRepository;
import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AnswerReader {

    private final AnswerRepository answerRepository;

    public List<Answer> getAnswers(UUID questionId) {
        return answerRepository.findHistory(questionId);
    }

    public Answer getAnswer(UUID answerId, UUID memberId) {
        return answerRepository.findOwned(answerId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANSWER_NOT_FOUND));
    }
}
