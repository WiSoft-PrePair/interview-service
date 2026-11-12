package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @Query("""
            SELECT COUNT(q) > 0
            FROM Question q
            WHERE q.memberId = :memberId
                AND q.assignedDate = :today
            """)
    boolean isAssigned(@Param("memberId") UUID memberId,
                       @Param("today") LocalDate today);
}
