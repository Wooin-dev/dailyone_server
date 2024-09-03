package com.wooin.dailyone.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@ToString
@Table(indexes = {
        @Index(name = "goal_id_idx", columnList = "goal_id"),
        @Index(name = "promise_goal_id_idx", columnList = "promise_goal_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedOfGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "promise_goal_id")
    private PromiseGoal promiseGoal;


    @Enumerated(EnumType.STRING)
    private FeedType feedType;

    @Type(JsonType.class) // 인덱싱 가능하게 jsonb타입으로 저장
    @Column(columnDefinition = "json")
    private FeedArgs feedArgs;
    //별도의 클래스를 따로 빼서 json으로 저장하게 한다. 이는 변동성이 높은 필드인 경우 확장성이 좋다.
    // 여러 필드를 만들어도 null이 많은 경우에도 해당방법으로 좀더 용이하게 처리가능하다


    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modifiedAt")
    private Timestamp modifiedAt;

    @PrePersist
    void createdAt() { this.createdAt = Timestamp.from(Instant.now()); }
    @PreUpdate
    void modifiedAt() { this.modifiedAt = Timestamp.from(Instant.now()); }

    @Builder
    public FeedOfGoal(Long id,
                      Goal goal,
                      PromiseGoal promiseGoal,
                      FeedType feedType,
                      FeedArgs feedArgs,
                      Timestamp createdAt,
                      Timestamp modifiedAt) {
        this.id = id;
        this.goal = goal;
        this.promiseGoal = promiseGoal;
        this.feedType = feedType;
        this.feedArgs = feedArgs;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
