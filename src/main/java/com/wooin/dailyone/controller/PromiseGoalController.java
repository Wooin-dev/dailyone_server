package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.PromiseGoalCreateRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.promisegoal.MyPromiseGoalResponse;
import com.wooin.dailyone.service.PromiseGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promise-goals")
@RequiredArgsConstructor
public class PromiseGoalController {

    private final PromiseGoalService promiseGoalService;

    @PostMapping("/{goalId}")
    public Response<Void> createPromiseGoal(@RequestBody PromiseGoalCreateRequest request, Authentication authentication) {
        promiseGoalService.createPromiseGoal(request, authentication.getName());
        return Response.success();
    }
    @GetMapping("/my")
    public Response<MyPromiseGoalResponse> selectMyPromiseGoal(Authentication authentication) {
        MyPromiseGoalResponse myPromiseGoalDto = promiseGoalService.selectMyPromiseGoal(authentication.getName());
        return Response.success(myPromiseGoalDto);
    }

    @DeleteMapping("/my")
    public Response<Void> deleteMyPromiseGoal(Authentication authentication) {
        promiseGoalService.deleteMyPromiseGoal(authentication.getName());
        return Response.success();
    }

}
