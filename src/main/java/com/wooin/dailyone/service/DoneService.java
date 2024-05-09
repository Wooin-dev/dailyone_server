package com.wooin.dailyone.service;

import com.wooin.dailyone.dto.DoneDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.DoneRepository;
import com.wooin.dailyone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoneService {

    private final UserRepository userRepository;
    private final DoneRepository doneRepository;


    public List<DoneDto> getDoneOfDayDetailList(String email, LocalDateTime createdAt) {

        User user = findUserByEmail(email);
        //받은 UTC를 바탕으로 한국기준 오늘의 시작과 끝 설정
        ZonedDateTime createdAtKR = createdAt.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        LocalDateTime startOfDateKR = createdAtKR.toLocalDate().atStartOfDay();
        LocalDateTime endOfDateKR = startOfDateKR.plusDays(1);

        log.debug("startOfDateKR = " + startOfDateKR);
        log.debug("endOfDateKR = " + endOfDateKR);

        List<Done> dones = doneRepository.findByUserAndCreatedAtBetween(user, startOfDateKR, endOfDateKR);
        return dones.stream().map(DoneDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<DoneDto> getDoneOfMonthList(String email, String yearMonth) {
        log.debug("yearMonth = " + yearMonth);
        log.debug("email = " + email);
        User user = findUserByEmail(email);
        //yearMonth ("YYYY-MM")
        int year = Integer.parseInt(yearMonth.split("-")[0]);
        int month = Integer.parseInt(yearMonth.split("-")[1]);

        LocalDateTime ldtStartOfMonth= LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime ldtEndOfMonth= LocalDateTime.of(year, month+1, 1, 0, 0);

        List<Done> dones = doneRepository.findByUserAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(user, ldtStartOfMonth, ldtEndOfMonth);
        log.debug("dones.size() = " + dones.size());
        return dones.stream().map(DoneDto::fromEntity).toList();
    }


    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s not found", email)));
    }
}
