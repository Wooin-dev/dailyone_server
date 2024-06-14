package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.done.DoneCountResponse;
import com.wooin.dailyone.controller.response.done.DoneDetailListResponse;
import com.wooin.dailyone.controller.response.done.DoneOfMonthArrayResponse;
import com.wooin.dailyone.dto.DoneDto;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.service.DoneService;
import com.wooin.dailyone.util.ClassUtils;
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

    @GetMapping("/count/{promiseGoalId}")
    public Response<DoneCountResponse> doneCount(@PathVariable Long promiseGoalId) {
        int doneCount = doneService.countDoneByPromiseGoalId(promiseGoalId);
        return Response.success(new DoneCountResponse(promiseGoalId, doneCount));
    }


    @GetMapping("/date")
    public Response<DoneDetailListResponse> getDoneOfDayDetailList(Authentication authentication,
                                                                   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
                                                                   @RequestParam("createdAt") LocalDateTime createdAt) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication, UserDto.class);
        List<DoneDto> doneDtos = doneService.getDoneOfDayDetailList(userDto.id(), createdAt);
        return Response.success(DoneDetailListResponse.of(doneDtos));
    }

    @GetMapping("/month")
    public Response<DoneOfMonthArrayResponse> getDoneOfMonthList(Authentication authentication,
                                                                 @RequestParam("yearMonth") String yearMonth) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication, UserDto.class);
        List<DoneDto> doneDtos = doneService.getDoneOfMonthList(userDto.id(), yearMonth);
        return Response.success(new DoneOfMonthArrayResponse(doneDtos));
    }

}
