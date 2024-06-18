package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.goal.GeneratedSimpleGoalResponse;
import com.wooin.dailyone.controller.response.goal.MyGoalListResponse;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.service.GoalService;
import com.wooin.dailyone.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public Response<Void> createGoal(@RequestBody GoalCreateRequest request, Authentication authentication) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class);
        goalService.create(request, userDto.id());
        return Response.success();
    }

    @GetMapping("/my")
    public Response<MyGoalListResponse> selectMyGoal(Authentication authentication) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class);
        MyGoalListResponse myGoalListResponse = goalService.selectMyGoal(userDto.id());
        return Response.success(myGoalListResponse);
    }

    @DeleteMapping("/my")
    public Response<Void> deleteMyAllGoal(Authentication authentication) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class);
        goalService.deleteGoal(userDto.id());
        return Response.success();
    }

    @GetMapping("generate-simple")
    public Response<GeneratedSimpleGoalResponse> generatedSimpleGoal(@RequestParam String originalGoal) {
        GeneratedSimpleGoalResponse response = goalService.generateSimpleGoal(originalGoal);
        return Response.success(response);
    }
}
