package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.entity.QuestionPool;
import io.wisoft.prepair.interview.question.entity.enums.Job;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionPoolRepository extends JpaRepository<QuestionPool, UUID> {

    // 같은 직무에서 최소 유사도를 충족하는 질문을 가까운 순서로 제한 조회한다.
    @Query("""
            SELECT pool
            FROM QuestionPool pool
            WHERE pool.job = :job
                AND cosine_distance(pool.embedding, :embedding) <= 1.0 - :minSimilarity
            ORDER BY cosine_distance(pool.embedding, :embedding) ASC
            LIMIT :candidateLimit
            """)
    List<QuestionPool> findSimilar(@Param("job") Job job,
                                   @Param("embedding") float[] embedding,
                                   @Param("minSimilarity") double minSimilarity,
                                   @Param("candidateLimit") int candidateLimit);

    @Query("""
            SELECT pool
            FROM QuestionPool pool
            WHERE pool.job = :job
                AND pool.questionTag = :tag
                AND NOT EXISTS (
                    SELECT 1
                    FROM Question q
                    WHERE q.memberId = :memberId
                        AND q.questionPool.id = pool.id)
            ORDER BY function('random')
            LIMIT 1
            """)
    Optional<QuestionPool> pickUnassigned(@Param("job") Job job,
                                          @Param("tag") QuestionTag tag,
                                          @Param("memberId") UUID memberId);
}
