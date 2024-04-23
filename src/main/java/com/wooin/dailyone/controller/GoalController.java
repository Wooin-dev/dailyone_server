package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    @PostMapping
    public Response<Void> create(@RequestBody GoalCreateRequest request, Authentication authentication) {
        goalService.create(GoalDto.from(request), authentication.getName());
        return Response.success();
    }
}
