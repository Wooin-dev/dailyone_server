package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.superdone.SuperDoneCountResponse;
import com.wooin.dailyone.controller.response.superdone.SuperDoneDetailListResponse;
import com.wooin.dailyone.controller.response.superdone.SuperDoneOfMonthArrayResponse;
import com.wooin.dailyone.dto.SuperDoneDto;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.service.SuperDoneService;
import com.wooin.dailyone.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/super-done")
@RequiredArgsConstructor
public class SuperDoneController {

    private final SuperDoneService superDoneService;


    @PostMapping("/{promiseGoalId}")
    public Response<Void> createSuperDone(@PathVariable Long promiseGoalId, Authentication authentication) {
        superDoneService.createSuperDone(authentication.getName(), promiseGoalId);
        return Response.success();
    }

    @GetMapping("/count/{promiseGoalId}")
    public Response<SuperDoneCountResponse> superDoneCount(@PathVariable Long promiseGoalId) {
        int doneCount = superDoneService.countSuperDoneByPromiseGoalId(promiseGoalId);
        return Response.success(new SuperDoneCountResponse(promiseGoalId, doneCount));
    }


    @GetMapping("/date")
    public Response<SuperDoneDetailListResponse> getDoneOfDayDetailList(Authentication authentication,
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
                                                                        @RequestParam("createdAt") LocalDateTime createdAt) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class);
        List<SuperDoneDto> superDoneDtoList = superDoneService.getSuperDoneOfDayDetailList(userDto.id(), createdAt);
        return Response.success(SuperDoneDetailListResponse.of(superDoneDtoList));
    }

    @GetMapping("/month")
    public Response<SuperDoneOfMonthArrayResponse> getDoneOfMonthList(Authentication authentication,
                                                                      @RequestParam("yearMonth") String yearMonth) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class);
        List<SuperDoneDto> superDoneDtoList = superDoneService.getSuperDoneOfMonthList(userDto.id(), yearMonth);
        return Response.success(new SuperDoneOfMonthArrayResponse(superDoneDtoList));
    }

}
