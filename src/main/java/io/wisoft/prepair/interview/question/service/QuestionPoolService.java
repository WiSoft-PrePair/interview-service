package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.external.openai.OpenAiQuestionClient.GeneratedQuestion;
import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.repository.QuestionPoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuestionPoolService {

    private final QuestionPoolRepository poolRepository;

    @Transactional
    public void save(Job job, List<GeneratedQuestion> questions, List<float[]> embeddings, Set<Integer> duplicateIndexes) {
        List<QuestionPool> pools = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            if (duplicateIndexes.contains(i)) continue;

            pools.add(new QuestionPool(
                    job,
                    questions.get(i).question(),
                    questions.get(i).questionTag(),
                    embeddings.get(i)
            ));
        }

        poolRepository.saveAll(pools);
    }
}
