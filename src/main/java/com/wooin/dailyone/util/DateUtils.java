package com.wooin.dailyone.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
public class DateUtils {

    public static LocalDate getLocalDateKSTfromUTC(LocalDateTime ldtUTC) {
        ZonedDateTime ldtKST = ldtUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return ldtKST.toLocalDate();
    }
}
