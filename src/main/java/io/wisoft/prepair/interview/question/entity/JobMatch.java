package io.wisoft.prepair.interview.question.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "job_match")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobMatch {

    @Id
    @Column(name = "member_id")
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "input", nullable = false)
    private String input;

    public JobMatch(UUID memberId, Job job, String input) {
        this.memberId = memberId;
        this.job = job;
        this.input = input;
    }
}
