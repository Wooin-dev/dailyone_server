package com.wooin.dailyone.service;

import com.wooin.dailyone.dto.DoneDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.DoneRepository;
import com.wooin.dailyone.repository.GoalRepository;
import com.wooin.dailyone.repository.PromiseGoalRepository;
import com.wooin.dailyone.repository.UserRepository;
import com.wooin.dailyone.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoneService {
    private final GoalRepository goalRepository;

    private final UserRepository userRepository;
    private final DoneRepository doneRepository;
    private final PromiseGoalRepository promiseGoalRepository;


    @Transactional
    public void createDone(String email, Long promiseGoalId) {
        // PromiseGoal Exist
        PromiseGoal promiseGoal = findPromiseGoalById(promiseGoalId);
        // Check already DONE : 이미 오늘 DONE처리 되어있는지
        throwIfDoneAlreadyToday(email, promiseGoal);
        // Save DONE
        Done todayDone = Done.builder().promiseGoal(promiseGoal).build();
        doneRepository.save(todayDone);
    }

    @Transactional(readOnly = true)
    public int countDoneByPromiseGoalId(Long promiseGoalId) {
        // Count DONE
        return doneRepository.countByPromiseGoal_Id(promiseGoalId);
    }

    @Transactional(readOnly = true)
    public List<DoneDto> getDoneOfDayDetailList(Long userId, LocalDateTime createdAt) {
        //받은 UTC를 바탕으로 한국기준 오늘의 시작과 끝 설정
        LocalDateTime startOfDateKR = DateUtils.getLocalDateKSTfromUTC(createdAt).atStartOfDay();
        LocalDateTime endOfDateKR = startOfDateKR.plusDays(1);

        log.debug("startOfDateKR = " + startOfDateKR);
        log.debug("endOfDateKR = " + endOfDateKR);

        List<Done> dones = doneRepository.findByPromiseGoal_User_IdAndCreatedAtBetween(userId, startOfDateKR, endOfDateKR);
        return dones.stream().map(DoneDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<DoneDto> getDoneOfMonthList(Long userId, String yearMonth) {
        //yearMonth ("YYYY-MM")
        int year = Integer.parseInt(yearMonth.split("-")[0]);
        int month = Integer.parseInt(yearMonth.split("-")[1]);
        LocalDateTime ldtStartOfMonth= LocalDateTime.of(year, month, 1, 0, 0); //TODO 시간대 체크. ec2 시간대설정 이슈 발생시
        LocalDateTime ldtEndOfMonth= LocalDateTime.of(year, month+1, 1, 0, 0);

        List<Done> dones = doneRepository.findByPromiseGoal_User_IdAndCreatedAtBetween(userId, ldtStartOfMonth, ldtEndOfMonth);
        return dones.stream().map(DoneDto::fromEntity).toList();
    }


    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s not found", email)));
    }
    private Goal findGoalById(Long goalId) {
        return goalRepository.findById(goalId).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("Goal of %s not found", goalId)));
    }
    private PromiseGoal findPromiseGoalById(Long promiseGoalId) {
        return promiseGoalRepository.findById(promiseGoalId).orElseThrow(() ->
                new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FOUND, String.format("PromiseGoal of %s is not found", promiseGoalId)));
    }

    private PromiseGoal findPromiseGoalByUser(User user) {
        return promiseGoalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("The goal of %s is not found", user)));
    }

    private void throwIfDoneAlreadyToday(String email, PromiseGoal promiseGoal) {
        Optional<Done> done = findTodayDoneByPromiseGoal(promiseGoal);
        done.ifPresent((it) -> {
            throw new DailyoneException(ErrorCode.ALREADY_DONE, String.format("email %s already DONE today goal of %d", email, promiseGoal.getId()));
        });
    }

    public Optional<Done> findTodayDoneByPromiseGoal(PromiseGoal promiseGoal) {
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();
        // 오늘의 시작 시간 ( 00:00:00 )
        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
        // 오늘의 종료 시간 ( 23:59:59 )
        Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX));

        return doneRepository.findByPromiseGoalAndCreatedAtBetween(promiseGoal, startOfDay, endOfDay);
    }
}
