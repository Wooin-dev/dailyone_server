package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.response.MyGoalResponse;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.model.Goal;
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
    public Response<Void> create(@RequestBody GoalCreateRequest request, Authentication authentication) {
        goalService.create(GoalDto.fromRequest(request), authentication.getName());
        return Response.success();
    }

    @GetMapping("/my")
    public Response<MyGoalResponse> my(Authentication authentication) {
        GoalDto myGoalDto = goalService.selectMyGoal(authentication.getName());
        return Response.success(MyGoalResponse.from(myGoalDto));
    }

    @DeleteMapping("/my")
    public Response<Void> deleteMyGoal(Authentication authentication) {
        goalService.deleteMyGoal(authentication.getName());
        return Response.success();
    }

    @PostMapping("/{goalId}/done")
    public Response<Void> done(@PathVariable Long goalId, Authentication authentication) {
        goalService.done(goalId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{goalId}/done-count")
    public Response<Void> doneCount(@PathVariable Long goalId, Authentication authentication) {
        goalService.doneCountByGoalIdAndEmail(goalId, authentication.getName());
        return Response.success();
    }
}
