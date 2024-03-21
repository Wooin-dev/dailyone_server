package com.wooin.dailyone.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "originalGoal"),
        @Index(columnList = "motivationComment"),
        @Index(columnList = "congratsComment"),
        @Index(columnList = "createdAt")
})
public class Goal extends DefaultEntity{

    @NotBlank
    @Setter private String originalGoal;

    @Setter private String simpleGoal;

    @Setter private String motivationComment;

    @Setter private String congratsComment;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
