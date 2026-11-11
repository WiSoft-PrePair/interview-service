package io.wisoft.prepair.interview.question.legacy.service;

import io.wisoft.prepair.interview.question.legacy.entity.InterviewQuestion;
import io.wisoft.prepair.interview.session.entity.InterviewSession;
import io.wisoft.prepair.interview.question.legacy.entity.enums.QuestionType;
import io.wisoft.prepair.interview.external.openai.dto.QuestionWithTags;
import io.wisoft.prepair.interview.question.legacy.repository.LegacyQuestionRepository;

import java.util.UUID;

import io.wisoft.prepair.interview.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionPersistenceService {

    private final LegacyQuestionRepository questionRepository;
    private final SessionRepository sessionRepository;

    public InterviewQuestion saveTodayQuestion(UUID memberId, QuestionWithTags result) {
        return save(memberId,
                QuestionType.TEXT,
                result,
                null);
    }

    public InterviewQuestion saveVideoQuestion(UUID memberId, QuestionWithTags result, InterviewSession session) {
        return save(memberId,
                QuestionType.VIDEO,
                result,
                session);
    }

    private InterviewQuestion save(UUID memberId, QuestionType questionType, QuestionWithTags result, InterviewSession session) {
        InterviewQuestion question = new InterviewQuestion(
                memberId,
                result.question(),
                questionType,
                result.joinTags(),
                session
        );
        return questionRepository.save(question);
    }

    public InterviewSession createSession(UUID memberId, int totalQuestionCount) {
        return sessionRepository.save(new InterviewSession(memberId, totalQuestionCount));
    }
}
