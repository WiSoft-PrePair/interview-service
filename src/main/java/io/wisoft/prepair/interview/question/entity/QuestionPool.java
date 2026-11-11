package io.wisoft.prepair.interview.question.entity;

import io.wisoft.prepair.interview.common.support.BaseTimeEntity;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Table(name = "question_pool")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionPool extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionTag questionTag;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    private float[] embedding;

    public QuestionPool(Job job, String question, QuestionTag questionTag, float[] embedding) {
        this.job = job;
        this.question = question;
        this.questionTag = questionTag;
        this.embedding = embedding;
    }
}
