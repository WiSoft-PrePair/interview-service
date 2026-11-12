package io.wisoft.prepair.interview.legacy.repository;

import io.wisoft.prepair.interview.legacy.entity.InterviewQuestion;
import io.wisoft.prepair.interview.legacy.entity.enums.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LegacyQuestionRepository extends JpaRepository<InterviewQuestion, UUID> {

    List<InterviewQuestion> findByMemberIdAndQuestionTypeOrderByCreatedAtDesc(UUID memberId, QuestionType questionType);

    Optional<InterviewQuestion> findByIdAndMemberId(UUID id, UUID memberId);

    Optional<InterviewQuestion> findByIdAndMemberIdAndInterviewSessionId(UUID id, UUID memberId, UUID sessionId);

    List<InterviewQuestion> findByInterviewSessionIdOrderByCreatedAtAsc(UUID sessionId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE InterviewQuestion q SET q.latestScore = :score " +
           "WHERE q.id = :id " +
           "AND q.latestScore IS NULL " +
           "AND q.questionType = io.wisoft.prepair.interview.legacy.entity.enums.QuestionType.TEXT " +
           "AND q.createdAt >= :startOfDay AND q.createdAt < :endOfDay")
    int updateLatestScoreIfFirstTime(
            @Param("id") UUID id,
            @Param("score") Integer score,
            @Param("startOfDay") Instant startOfDay,
            @Param("endOfDay") Instant endOfDay);
}
