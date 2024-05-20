package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.done.DoneCountResponse;
import com.wooin.dailyone.controller.response.done.DoneDetailListResponse;
import com.wooin.dailyone.controller.response.done.DoneOfMonthArrayResponse;
import com.wooin.dailyone.dto.DoneDto;
import com.wooin.dailyone.service.DoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/done")
@RequiredArgsConstructor
public class DoneController {

    private final DoneService doneService;


    @PostMapping("/{promiseGoalId}")
    public Response<Void> createDone(@PathVariable Long promiseGoalId, Authentication authentication) {
        doneService.createDone(authentication.getName(), promiseGoalId);
        return Response.success();
    }

    @GetMapping("/count/{goalId}")
    public Response<DoneCountResponse> doneCount(@PathVariable Long goalId, Authentication authentication) {
        Integer doneCount = doneService.countDoneByEmailAndGoalId(authentication.getName(), goalId);
        return Response.success(new DoneCountResponse(doneCount));
    }


    @GetMapping("/date")
    public Response<DoneDetailListResponse> getDoneOfDayDetailList(Authentication authentication,
                                                                   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
                                                         @RequestParam("createdAt") LocalDateTime createdAt) {
        List<DoneDto> doneDtos = doneService.getDoneOfDayDetailList(authentication.getName(), createdAt);
        return Response.success(DoneDetailListResponse.of(doneDtos));
    }

    @GetMapping("/month")
    public Response<DoneOfMonthArrayResponse> getDoneOfMonthList(Authentication authentication,
                                                         @RequestParam("yearMonth") String yearMonth) {
        List<DoneDto> doneDtos = doneService.getDoneOfMonthList(authentication.getName(), yearMonth);
        return Response.success(new DoneOfMonthArrayResponse(doneDtos));
    }

}
