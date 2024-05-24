package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.superdone.SuperDoneCountResponse;
import com.wooin.dailyone.controller.response.superdone.SuperDoneDetailListResponse;
import com.wooin.dailyone.controller.response.superdone.SuperDoneOfMonthArrayResponse;
import com.wooin.dailyone.dto.SuperDoneDto;
import com.wooin.dailyone.service.SuperDoneService;
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

    @GetMapping("/count/{goalId}")
    public Response<SuperDoneCountResponse> doneCount(@PathVariable Long goalId, Authentication authentication) {
        Integer doneCount = superDoneService.countSuperDoneByEmailAndGoalId(authentication.getName(), goalId);
        return Response.success(new SuperDoneCountResponse(doneCount));
    }


    @GetMapping("/date")
    public Response<SuperDoneDetailListResponse> getDoneOfDayDetailList(Authentication authentication,
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
                                                         @RequestParam("createdAt") LocalDateTime createdAt) {
        List<SuperDoneDto> superDoneDtoList = superDoneService.getSuperDoneOfDayDetailList(authentication.getName(), createdAt);
        return Response.success(SuperDoneDetailListResponse.of(superDoneDtoList));
    }

    @GetMapping("/month")
    public Response<SuperDoneOfMonthArrayResponse> getDoneOfMonthList(Authentication authentication,
                                                                      @RequestParam("yearMonth") String yearMonth) {
        List<SuperDoneDto> superDoneDtoList = superDoneService.getSuperDoneOfMonthList(authentication.getName(), yearMonth);
        return Response.success(new SuperDoneOfMonthArrayResponse(superDoneDtoList));
    }

}
