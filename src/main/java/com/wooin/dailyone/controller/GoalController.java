package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.goal.MyGoalResponse;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.service.GoalService;
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
        goalService.create(request, authentication.getName());
        return Response.success();
    }

    @GetMapping("/my")
    public Response<MyGoalResponse> selectMyGoal(Authentication authentication) {
        GoalDto myGoalDto = goalService.selectMyGoal(authentication.getName());
        return Response.success(MyGoalResponse.from(myGoalDto));
    }

    @DeleteMapping("/my")
    public Response<Void> deleteMyGoal(Authentication authentication) {
        goalService.deleteGoal(authentication.getName());
        return Response.success();
    }

}
