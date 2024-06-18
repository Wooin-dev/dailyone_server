package com.wooin.dailyone.model;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE goal SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
@Table(indexes = {
        @Index(columnList = "originalGoal"),
        @Index(columnList = "createdAt")
})
@Entity
public class Goal extends DefaultEntity {

    @Setter
    @Column(nullable = false)
    private String originalGoal;

    @Setter
    private String simpleGoal;

    @Setter
    @Column(nullable = false, length = 1000)
    private String motivationComment;

    @Setter
    @Column(nullable = false, length = 1000)
    private String congratsComment;

    @Column(name = "deleted_at")
    private Timestamp deletedAt; //소프트 삭제를 위한 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Goal(String originalGoal, String simpleGoal, String motivationComment, String congratsComment, User user) {
        this.originalGoal = originalGoal;
        this.simpleGoal = simpleGoal;
        this.motivationComment = motivationComment;
        this.congratsComment = congratsComment;
        this.user = user;
    }
    public static GoalBuilder builderFromRequest(GoalCreateRequest request) {
        return Goal.builder()
                .simpleGoal(request.getSimpleGoal())
                .originalGoal(request.getOriginalGoal())
                .congratsComment(request.getCongratsComment())
                .motivationComment(request.getMotivationComment());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goal goal)) return false;
        return this.getId() != null && this.getId().equals(((Goal) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
