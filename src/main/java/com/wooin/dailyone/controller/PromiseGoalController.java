package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.PromiseGoalCreateRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.promisegoal.MyPromiseGoalListResponse;
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
    @GetMapping("/my") //TODO 추후에 페이징 처리하거나 개인 목표등록의 개수제한이 필요해 보인다.
    public Response<MyPromiseGoalListResponse> selectMyPromiseGoal(Authentication authentication) {
        MyPromiseGoalListResponse myPromiseGoalListResponse = promiseGoalService.selectMyPromiseGoal(authentication.getName());
        return Response.success(myPromiseGoalListResponse);
    }

    @DeleteMapping("/my")
    public Response<Void> deleteMyPromiseGoal(Authentication authentication) {
        promiseGoalService.deleteMyPromiseGoal(authentication.getName());
        return Response.success();
    }

    @DeleteMapping("/{promiseGoalId}")
    public Response<Void> deletePromiseGoal(@PathVariable Long promiseGoalId) {
        promiseGoalService.deletePromiseGoal(promiseGoalId);
        return Response.success();
    }

    @GetMapping("/finish/{promiseGoalId}")
    public Response<Void> finishPromiseGoal(Authentication authentication, @PathVariable Long promiseGoalId) {
        promiseGoalService.finishPromiseGoal(authentication.getName(), promiseGoalId);
        return Response.success();
    }
}
