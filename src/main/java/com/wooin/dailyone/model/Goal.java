package com.wooin.dailyone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "originalGoal"),
        @Index(columnList = "motivationComment"),
        @Index(columnList = "congratsComment"),
        @Index(columnList = "createdAt")
})
@Entity
public class Goal extends DefaultEntity{

    @NotBlank
    @Setter @Column(nullable = false)   private String originalGoal;
    @Setter                             private String simpleGoal;
    @Setter @Column(nullable = false)   private String motivationComment;
    @Setter @Column(nullable = false)   private String congratsComment;

    @ManyToOne                          private User user;

    private Goal(String originalGoal, String simpleGoal, String motivationComment, String congratsComment) {
        super();
        this.originalGoal = originalGoal;
        this.simpleGoal = simpleGoal;
        this.motivationComment = motivationComment;
        this.congratsComment = congratsComment;
    }

    public static Goal of(String originalGoal, String simpleGoal, String motivationComment, String congratsComment) {
        return new Goal(originalGoal, simpleGoal, motivationComment, congratsComment);
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
