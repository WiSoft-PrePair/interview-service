package io.wisoft.prepair.interview.answer.service;

import io.wisoft.prepair.interview.answer.dto.response.AnswerDetail;
import io.wisoft.prepair.interview.answer.entity.Answer;
import io.wisoft.prepair.interview.external.openai.client.OpenAiFeedbackClient;
import io.wisoft.prepair.interview.external.openai.dto.GeneratedFeedback;
import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.service.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AnswerSubmitter {

    private final OpenAiFeedbackClient feedbackClient;
    private final QuestionReader questionReader;
    private final AnswerWriter answerWriter;

    public AnswerDetail submit(UUID questionId, UUID memberId, String answer) {
        Question question = questionReader.getQuestion(questionId, memberId);
        GeneratedFeedback feedback = feedbackClient.generate(
                question.getQuestionTag(), question.getQuestion(), answer);

        Answer result = answerWriter.save(question, answer, feedback);

        return AnswerDetail.from(result);
    }
}
