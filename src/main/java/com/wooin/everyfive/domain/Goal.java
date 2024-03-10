package com.wooin.everyfive.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Goal extends DefaultEntity{

    @NotBlank
    private String originalGoal;

    private String simpleGoal;

    private String motivationComment;

    private String congratsComment;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
