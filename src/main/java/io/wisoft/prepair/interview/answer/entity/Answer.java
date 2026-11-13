package io.wisoft.prepair.interview.answer.entity;

import io.wisoft.prepair.interview.common.support.BaseTimeEntity;
import io.wisoft.prepair.interview.question.entity.Question;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String good;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String improvement;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String example;

    public Answer(
            Question question, String answer, Integer score,
            String summary, String good, String improvement, String example
    ) {
        this.question = question;
        this.answer = answer;
        this.score = score;
        this.summary = summary;
        this.good = good;
        this.improvement = improvement;
        this.example = example;
    }
}
