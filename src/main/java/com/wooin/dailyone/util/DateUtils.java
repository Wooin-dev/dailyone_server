package com.wooin.dailyone.util;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
public class DateUtils {

    public static LocalDate getLocalDateKSTfromUTC(@Nullable LocalDateTime ldtUTC) {
        //null 체크
        if(ldtUTC==null) return null;

        //ZDT로 변환
        ZonedDateTime ldtKST = ldtUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return ldtKST.toLocalDate();
    }
}
