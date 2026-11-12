package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.dto.TagCount;
import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
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

    @Query("""
            SELECT q
            FROM Question q
            WHERE q.memberId = :memberId
                AND q.questionTag = COALESCE(:tag, q.questionTag)
                AND LOWER(REPLACE(q.question, ' ', '')) LIKE CONCAT('%', COALESCE(:keyword, ''), '%') ESCAPE '\\'
                AND (CAST(:cursor AS LocalDate) IS NULL OR q.assignedDate < :cursor)
            ORDER BY q.assignedDate DESC
            """)
    Slice<Question> findHistory(@Param("memberId") UUID memberId,
                                @Param("tag") QuestionTag tag,
                                @Param("keyword") String keyword,
                                @Param("cursor") LocalDate cursor,
                                Pageable pageable);

    @Query("""
            SELECT new io.wisoft.prepair.interview.question.dto.TagCount(q.questionTag, COUNT(q))
            FROM Question q
            WHERE q.memberId = :memberId
            GROUP BY q.questionTag
            """)
    List<TagCount> countByTag(@Param("memberId") UUID memberId);
}
