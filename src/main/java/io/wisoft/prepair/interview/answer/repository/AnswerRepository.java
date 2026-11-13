package io.wisoft.prepair.interview.answer.repository;

import io.wisoft.prepair.interview.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {

    @Query("""
            SELECT a
            FROM Answer a
            WHERE a.question.id = :questionId
            ORDER BY a.createdAt DESC
            """)
    List<Answer> findHistory(@Param("questionId") UUID questionId);

    @Query("""
            SELECT a
            FROM Answer a
            WHERE a.id = :answerId
                AND a.question.memberId = :memberId
            """)
    Optional<Answer> findOwned(@Param("answerId") UUID answerId,
                               @Param("memberId") UUID memberId);
}
