package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
