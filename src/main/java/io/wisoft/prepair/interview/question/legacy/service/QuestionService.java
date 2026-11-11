package io.wisoft.prepair.interview.question.legacy.service;

import io.wisoft.prepair.interview.question.legacy.entity.InterviewQuestion;
import io.wisoft.prepair.interview.question.legacy.dto.VideoInterviewRequest;
import io.wisoft.prepair.interview.question.legacy.dto.QuestionResponse;
import io.wisoft.prepair.interview.session.entity.InterviewSession;
import io.wisoft.prepair.interview.question.legacy.entity.enums.QuestionType;
import io.wisoft.prepair.interview.external.member.MemberServiceClient;
import io.wisoft.prepair.interview.common.exception.BusinessException;
import io.wisoft.prepair.interview.common.exception.ErrorCode;
import io.wisoft.prepair.interview.external.member.dto.MemberProfile;
import io.wisoft.prepair.interview.external.openai.OpenAiClient;
import io.wisoft.prepair.interview.external.openai.dto.QuestionWithTags;
import io.wisoft.prepair.interview.question.legacy.prompt.QuestionPromptBuilder;
import io.wisoft.prepair.interview.question.legacy.repository.LegacyQuestionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionPersistenceService questionPersistService;
    private final LegacyQuestionRepository questionRepository;
    private final MemberServiceClient memberServiceClient;
    private final OpenAiClient openAiClient;
    private final QuestionPromptBuilder promptBuilder;

    public List<QuestionResponse> getQuestions(UUID memberId, QuestionType type) {
        return questionRepository.findByMemberIdAndQuestionTypeOrderByCreatedAtDesc(memberId, type)
                .stream()
                .map(QuestionResponse::from)
                .toList();
    }

    public QuestionResponse getQuestion(UUID questionId, UUID memberId) {
        InterviewQuestion question = questionRepository.findByIdAndMemberId(questionId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        return QuestionResponse.from(question);
    }

    @Transactional
    public List<QuestionResponse> generateVideoQuestions(UUID memberId, VideoInterviewRequest request) {
        MemberProfile member = memberServiceClient.getMember(memberId);
        String prompt = promptBuilder.buildVideoQuestionPrompt(member.job(), request.count());

        List<QuestionWithTags> aiQuestions = openAiClient.generateQuestions(prompt);
        InterviewSession session = questionPersistService.createSession(memberId, aiQuestions.size());

        List<QuestionResponse> questionResponses = aiQuestions.stream()
                .map(aiQuestion -> questionPersistService.saveVideoQuestion(memberId, aiQuestion, session))
                .map(QuestionResponse::from)
                .toList();

        log.info("화상 면접 질문 생성 완료 - memberId: {}, sessionId: {}", memberId, session.getId());
        return questionResponses;
    }
}
