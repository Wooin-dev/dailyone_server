package com.wooin.dailyone.controller.response.goal;

import com.wooin.dailyone.dto.GoalDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GoalThumbResponse {

    private Long goalId;
    private String originalGoal;
    private String simpleGoal;
    private LocalDateTime createdAt;
    private String nickname;
    private Integer challengersCount;
    private Integer doneCount;
    private Integer viewCount;

    @Builder
    public GoalThumbResponse(Long goalId, String originalGoal, String simpleGoal, LocalDateTime createdAt, String nickname, Integer challengersCount, Integer doneCount, Integer viewCount) {
        this.goalId = goalId;
        this.originalGoal = originalGoal;
        this.simpleGoal = simpleGoal;
        this.createdAt = createdAt;
        this.nickname = nickname;
        this.challengersCount = challengersCount;
        this.doneCount = doneCount;
        this.viewCount = viewCount;
    }

    public static GoalThumbResponseBuilder builderFromDto(GoalDto goalDto) {
        return GoalThumbResponse.builder()
                .goalId(goalDto.id())
                .originalGoal(goalDto.originalGoal())
                .simpleGoal(goalDto.simpleGoal())
                .createdAt(goalDto.createdAt())
                .nickname(goalDto.userDto().nickname())
                .viewCount(goalDto.viewCount())
                ;
    }
}
