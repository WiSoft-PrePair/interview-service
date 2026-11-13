package io.wisoft.prepair.interview.question.entity;

import io.wisoft.prepair.interview.question.entity.enums.QuestionStatus;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import io.wisoft.prepair.interview.common.support.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Table(name = "question",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_question_member_pool",
                        columnNames = {"member_id", "question_pool_id"}
                ),

                @UniqueConstraint(
                        name = "uk_question_member_assigned_date",
                        columnNames = {"member_id", "assigned_date"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_pool_id", nullable = false)
    private QuestionPool questionPool;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionTag questionTag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus status = QuestionStatus.UNANSWERED;

    @Column(name = "best_score")
    private Integer bestScore;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    public Question(UUID memberId, QuestionPool questionPool, LocalDate assignedDate) {
        this.memberId = memberId;
        this.questionPool = questionPool;
        this.question = questionPool.getQuestion();
        this.questionTag = questionPool.getQuestionTag();
        this.assignedDate = assignedDate;
    }
}
