package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.entity.QuestionPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionPoolRepository extends JpaRepository<QuestionPool, UUID> {

    // 전체 풀 개수에서 해당 직무의 질문을 가장 많이 받은 회원의 개수를 뺀다.
    @Query(value = """
            SELECT CAST((
                SELECT COUNT(*)
                FROM question_pool
                WHERE job_id = :jobId
            ) - MAX(member_counts.received_count) AS INTEGER)
            FROM (
                SELECT COUNT(pool.id) AS received_count
                FROM job_match mapping
                LEFT JOIN question received
                    ON received.member_id = mapping.member_id
                LEFT JOIN question_pool pool
                    ON pool.id = received.question_pool_id
                    AND pool.job_id = mapping.job_id
                WHERE mapping.job_id = :jobId
                GROUP BY mapping.member_id
            ) member_counts
            """, nativeQuery = true)
    Integer getMinRemaining(@Param("jobId") UUID jobId);

    // 같은 직무에서 최소 유사도를 충족하는 질문을 가까운 순서로 제한 조회한다.
    @Query("""
            SELECT pool
            FROM QuestionPool pool
            WHERE pool.job.id = :jobId
                AND cosine_distance(pool.embedding, :embedding) <= 1.0 - :minSimilarity
            ORDER BY cosine_distance(pool.embedding, :embedding) ASC
            LIMIT :candidateLimit
            """)
    List<QuestionPool> findSimilar(@Param("jobId") UUID jobId,
                                   @Param("embedding") float[] embedding,
                                   @Param("minSimilarity") double minSimilarity,
                                   @Param("candidateLimit") int candidateLimit);

    @Query("""
            SELECT pool
            FROM QuestionPool pool
            WHERE pool.job.id = :jobId
                AND NOT EXISTS (
                    SELECT 1
                    FROM Question q
                    WHERE q.memberId = :memberId
                        AND q.questionPool.id = pool.id)
            ORDER BY function('random')
            LIMIT 1
            """)
    Optional<QuestionPool> pickUnassigned(@Param("jobId") UUID jobId,
                                          @Param("memberId") UUID memberId);
}
