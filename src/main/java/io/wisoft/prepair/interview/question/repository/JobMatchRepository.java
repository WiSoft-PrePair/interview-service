package io.wisoft.prepair.interview.question.repository;

import io.wisoft.prepair.interview.question.entity.JobMatch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobMatchRepository extends JpaRepository<JobMatch, UUID> {

    @Override
    @EntityGraph(attributePaths = "job")
    Optional<JobMatch> findById(UUID memberId);
}
