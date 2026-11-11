package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    Optional<Job> findByName(String name);

    @Query("""
            SELECT j
            FROM Job j
            WHERE cosine_distance(j.embedding, :embedding) <= 1.0 - :minSimilarity
            ORDER BY cosine_distance(j.embedding, :embedding) ASC
            LIMIT 1
            """)
    Optional<Job> findSimilarJob(@Param("embedding") float[] embedding,
                                 @Param("minSimilarity") double minSimilarity
    );
}
