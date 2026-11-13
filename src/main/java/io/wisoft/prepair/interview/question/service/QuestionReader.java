package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.question.dto.TagCount;
import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import io.wisoft.prepair.interview.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuestionReader {

    private final QuestionRepository questionRepository;

    public boolean isAssigned(UUID memberId, LocalDate today) {
        return questionRepository.isAssigned(memberId, today);
    }

    public Slice<Question> getQuestions(UUID memberId, QuestionTag tag, String keyword, LocalDate cursor, Pageable pageable) {
        return questionRepository.findHistory(memberId, tag, keyword, cursor, pageable);
    }

    public List<TagCount> countByTag(UUID memberId) {
        return questionRepository.countByTag(memberId);
    }

    public Question getQuestion(UUID questionId, UUID memberId) {
        return questionRepository.findOwned(questionId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
    }

//    public QuestionResponse getQuestion(UUID questionId, UUID memberId) {
//        InterviewQuestion question = questionRepository.findByIdAndMemberId(questionId, memberId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
//
//        return QuestionResponse.from(question);
//    }
}
