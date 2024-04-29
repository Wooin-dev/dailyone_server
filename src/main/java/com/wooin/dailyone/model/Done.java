package com.wooin.dailyone.model;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt; //소프트 삭제를 위한 필드



    @PrePersist
    void createdAt() {this.createdAt = Timestamp.from(Instant.now());}
    @PreUpdate
    void modifiedAt() {this.modifiedAt = Timestamp.from(Instant.now());}

    protected Done() {}
    public Done(User user, Goal goal) {
        this.user = user;
        this.goal = goal;
    }
}
