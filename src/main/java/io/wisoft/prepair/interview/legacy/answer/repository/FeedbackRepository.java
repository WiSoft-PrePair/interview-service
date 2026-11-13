package io.wisoft.prepair.interview.legacy.answer.repository;

import io.wisoft.prepair.interview.legacy.answer.entity.InterviewFeedback;
import io.wisoft.prepair.interview.legacy.answer.entity.FeedbackType;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<InterviewFeedback, UUID> {
    List<InterviewFeedback> findByInterviewAnswerId(UUID answerId);

    @Query("SELECT COUNT(f) FROM InterviewFeedback f " +
            "WHERE f.interviewAnswer.interviewQuestion.interviewSession.id = :sessionId " +
            "AND f.feedbackType = :feedbackType")
    long countBySessionIdAndFeedbackType(UUID sessionId, FeedbackType feedbackType);

    @Query("SELECT f FROM InterviewFeedback f " +
            "JOIN FETCH f.interviewAnswer a " +
            "JOIN FETCH a.interviewQuestion q " +
            "WHERE q.interviewSession.id = :sessionId")
    List<InterviewFeedback> findAllBySessionId(@Param("sessionId") UUID sessionId);
}
