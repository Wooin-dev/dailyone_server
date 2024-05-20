package com.wooin.dailyone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.User;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.wooin.dailyone.model.Goal}
 */
@Builder
public record GoalDto(
        Long id,
        String originalGoal,
        String simpleGoal,
        String motivationComment,
        String congratsComment,
        @JsonProperty("user")
        UserDto userDto,
        boolean isDoneToday,
        int doneCount,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy

) {

    //TODO : RequestDto를 굳이 변환해줄 필요가 있는지?
    public static GoalDto fromRequest(GoalCreateRequest request) {
        return GoalDto.builder()
                .originalGoal(request.getOriginalGoal())
                .simpleGoal(request.getSimpleGoal())
                .motivationComment(request.getMotivationComment())
                .congratsComment(request.getCongratsComment())
                .build();
    }

    public static GoalDto fromEntity(Goal goal) {
        return GoalDto.builder()
                .id(goal.getId())
                .originalGoal(goal.getOriginalGoal())
                .simpleGoal(goal.getSimpleGoal())
                .motivationComment(goal.getMotivationComment())
                .congratsComment(goal.getCongratsComment())
                .createdAt(goal.getCreatedAt())
                .modifiedAt(goal.getModifiedAt())
                .userDto(UserDto.fromEntity(goal.getUser()))
                .build();
    }

    //dto내부에 userDto필드가 null일수도 있으므로 dto.userDto()가 아닌 외부에서 받아오게 설계
    public Goal toEntity(User user) {
        return Goal.builder()
                .congratsComment(congratsComment)
                .originalGoal(originalGoal)
                .user(user)
                .motivationComment(motivationComment)
                .simpleGoal(simpleGoal)
                .build();
    }
}