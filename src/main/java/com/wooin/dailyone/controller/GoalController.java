package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.request.GoalFollowRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.goal.GeneratedSimpleGoalResponse;
import com.wooin.dailyone.controller.response.goal.GoalDetailResponse;
import com.wooin.dailyone.controller.response.goal.GoalThumbListResponse;
import com.wooin.dailyone.controller.response.goal.MyGoalListResponse;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.service.GoalService;
import com.wooin.dailyone.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public Response<Void> createGoal(@RequestBody GoalCreateRequest request, Authentication authentication) {
        System.out.println("authentication.getPrincipal().toString() = " + authentication.getPrincipal().toString());
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class);
        goalService.create(request, userDto.id());
        return Response.success();
    }

    @PostMapping("/follow")
    public Response<Void> followGoal(@RequestBody GoalFollowRequest request, Authentication authentication) {

        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class);
        goalService.followGoal(request, userDto.id());

        return Response.success();
    }

    @GetMapping("/thumbs")
    public Response<GoalThumbListResponse> selectGoalThumbPage(Pageable pageable) {
        GoalThumbListResponse goalThumbListResponse = goalService.selectGoalThumbPage(pageable);
        return Response.success(goalThumbListResponse);
    }

    @GetMapping("/{goalId}")
    public Response<GoalDetailResponse> selectGoal(@PathVariable Long goalId) {
        GoalDetailResponse goalDetailResponse = goalService.selectGoal(goalId);
        return Response.success(goalDetailResponse);
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
