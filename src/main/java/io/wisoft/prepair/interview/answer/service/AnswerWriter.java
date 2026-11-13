package io.wisoft.prepair.interview.answer.service;

import io.wisoft.prepair.interview.answer.entity.Answer;
import io.wisoft.prepair.interview.answer.repository.AnswerRepository;
import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.service.QuestionWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.wisoft.prepair.interview.external.openai.OpenAiFeedbackClient.*;

@Component
@Transactional
@RequiredArgsConstructor
public class AnswerWriter {

    private final AnswerRepository answerRepository;
    private final QuestionWriter questionWriter;

    public Answer save(Question question, String answer, GeneratedFeedback feedback) {
        questionWriter.answered(question.getId(), feedback.score());

        return answerRepository.save(new Answer(
                question,
                answer,
                feedback.score(),
                feedback.summary(),
                feedback.good(),
                feedback.improvement(),
                feedback.example())
        );
    }
}
