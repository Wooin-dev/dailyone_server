package com.wooin.dailyone.controller.response.superdone;

import com.wooin.dailyone.dto.SuperDoneDto;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class SuperDoneOfMonthArrayResponse {

    private String[] superDoneOfMonthArray;

    public SuperDoneOfMonthArrayResponse(List<SuperDoneDto> superDoneDtos) {

        Set<String> superDoneOfMonthSet = new HashSet<>();

        for (SuperDoneDto superDoneDto : superDoneDtos) {
            superDoneOfMonthSet.add(superDoneDto.createdAt().toString().substring(0, 10));
        }
        this.superDoneOfMonthArray = superDoneOfMonthSet.toArray(new String[0]); //배열의 크기를 0으로 하면 자동으로 설정해준다.
    }
}
