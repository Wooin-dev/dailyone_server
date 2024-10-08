package com.wooin.dailyone.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Table(name = "done")
@SQLDelete(sql = "UPDATE done SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class Done {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_goal_id")
    private PromiseGoal promiseGoal;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt; //소프트 삭제를 위한 필드


    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = Timestamp.from(Instant.now());
    }

    protected Done() {
    }

    @Builder
    private Done(Long id, PromiseGoal promiseGoal, Timestamp createdAt, Timestamp modifiedAt) {
        this.id = id;
        this.promiseGoal = promiseGoal;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
