package io.wisoft.prepair.interview.answer.repository;

import io.wisoft.prepair.interview.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
}
