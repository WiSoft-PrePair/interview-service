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

                // 하루 3개 제한 | 같은 날 같은 순번은 한 번만 (동시 요청 방지)
                @UniqueConstraint(
                        name = "uk_question_member_date_seq",
                        columnNames = {"member_id", "assigned_date", "daily_seq"}
                )
        },
        // 순번은 1~3만 (코드 한도 확인이 빠져도 DB가 막음)
        check = @CheckConstraint(
                name = "ck_question_daily_seq",
                constraint = "daily_seq BETWEEN 1 AND 3"
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    // 하루에 받을 수 있는 질문 수 (카테고리 합산, 한국 시간 자정 기준) | DB 제약 daily_seq 1~3과 같은 규칙
    public static final int DAILY_LIMIT = 3;

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

    // 그날 몇 번째로 받은 질문인지 (1~3)
    @Column(name = "daily_seq", nullable = false)
    private Integer dailySeq;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    public Question(UUID memberId, QuestionPool questionPool, LocalDate assignedDate, int dailySeq) {
        this.memberId = memberId;
        this.questionPool = questionPool;
        this.question = questionPool.getQuestion();
        this.questionTag = questionPool.getQuestionTag();
        this.assignedDate = assignedDate;
        this.dailySeq = dailySeq;
    }
}
