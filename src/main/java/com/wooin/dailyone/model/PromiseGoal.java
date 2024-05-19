package com.wooin.dailyone.model;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.util.DateUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE promise_goal SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
@Table(name = "promise_goal")
@Entity
public class PromiseGoal extends DefaultEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "promise_done_count") @Comment("목표로 하는 달성 수치. 이 값은 시작일과 종료일 사이의 기간을 초과할 수 없습니다.")
    private Integer promiseDoneCount;

    @Column(name = "deleted_at")
    private Timestamp deletedAt; //소프트 삭제를 위한 필드


    @Builder
    private PromiseGoal(User user, Goal goal, LocalDateTime startDate, LocalDateTime endDate, Integer promiseDoneCount) {
        this.user = user;
        this.goal = goal;
        this.startDate = DateUtils.getLocalDateKSTfromUTC(startDate);
        this.endDate = DateUtils.getLocalDateKSTfromUTC(endDate);
        this.promiseDoneCount = promiseDoneCount;
    }

    public static PromiseGoalBuilder builderFromRequest(GoalCreateRequest request) {
        return PromiseGoal.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .promiseDoneCount(request.getPromiseDoneCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromiseGoal promiseGoal)) return false;
        return this.getId() != null && this.getId().equals(((PromiseGoal) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }




}
