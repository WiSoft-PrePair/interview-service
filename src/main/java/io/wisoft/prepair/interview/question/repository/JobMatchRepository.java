package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.entity.Job;
import io.wisoft.prepair.interview.question.entity.JobMatch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JobMatchRepository extends JpaRepository<JobMatch, UUID> {

    @Override
    @EntityGraph(attributePaths = "job")
    Optional<JobMatch> findById(UUID memberId);

    @Query("""
            SELECT jm.job
            FROM JobMatch jm
            WHERE jm.memberId = :memberId
            """)
    Optional<Job> getJob(@Param("memberId") UUID memberId);
}
