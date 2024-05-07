package com.wooin.dailyone.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wooin.dailyone.controller.response.DoneDetailResponse;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.dto.DoneDto;
import com.wooin.dailyone.service.DoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/done")
@RequiredArgsConstructor
public class DoneController {

    private final DoneService doneService;
    @GetMapping("/date")
    public Response<DoneDetailResponse> getDoneOfDayList(Authentication authentication,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
                                                         @RequestParam("createdAt") LocalDateTime createdAt) {
        List<DoneDto> doneDtos = doneService.getDoneOfDayList(authentication.getName(), createdAt);
        return Response.success(new DoneDetailResponse(doneDtos));
    }

}
