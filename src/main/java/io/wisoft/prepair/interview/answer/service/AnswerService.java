package io.wisoft.prepair.interview.answer.service;

import io.wisoft.prepair.interview.answer.dto.response.AnswerDetail;
import io.wisoft.prepair.interview.answer.dto.response.AnswerPreview;
import io.wisoft.prepair.interview.answer.entity.Answer;
import io.wisoft.prepair.interview.question.service.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerReader answerReader;
    private final QuestionReader questionReader;

    public List<AnswerPreview> getAnswers(UUID questionId, UUID memberId) {
        questionReader.getQuestion(questionId, memberId);

        return answerReader.getAnswers(questionId).stream()
                .map(AnswerPreview::from)
                .toList();
    }

    public AnswerDetail getAnswer(UUID answerId, UUID memberId) {
        Answer answer = answerReader.getAnswer(answerId, memberId);
        return AnswerDetail.from(answer);
    }
}
