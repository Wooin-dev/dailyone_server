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
        UserDto userDto, //todo : Goal 객체에 있는 User 객체를 UserDto로 변환하여
        boolean isDoneToday,
        // int doneCount, // todo : Goal에는 없는 필드값을 가지고 있다보니 fromEntity()를 통해 생성시 null값으로 생성되기도 한다.
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        int viewCount,
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
                .createdBy(goal.getCreatedBy())
                .modifiedAt(goal.getModifiedAt())
                .viewCount(goal.getViewCount())
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
