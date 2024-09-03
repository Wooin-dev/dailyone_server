package com.wooin.dailyone.service;

import com.wooin.dailyone.dto.SuperDoneDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.*;
import com.wooin.dailyone.repository.*;
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
public class SuperDoneService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final DoneRepository doneRepository;
    private final SuperDoneRepository superDoneRepository;
    private final PromiseGoalRepository promiseGoalRepository;
    private final FeedRepository feedRepository;


    @Transactional
    public void createSuperDone(String email, Long promiseGoalId) {
        // PromiseGoal Exist
        PromiseGoal promiseGoal = promiseGoalRepository.getReferenceById(promiseGoalId);
        // Check already DONE : 이미 오늘 DONE처리 되어있는지
        throwIfNotDoneToday(email, promiseGoal);
        // Save DONE
        SuperDone todaySuperDone = SuperDone.builder().promiseGoal(promiseGoal).build();
        superDoneRepository.save(todaySuperDone);

        // Feed 생성
        //  : 쓰기에서 시간이 좀 걸리더라도 조회시 줄일 수 있는 부분이 있다면 채용하는 방향
        FeedOfGoal feed = FeedOfGoal.builder()
                .goal(promiseGoal.getGoal())
                .promiseGoal(promiseGoal)
                .feedType(FeedType.DO_SUPER_DONE)
                .feedArgs(FeedArgs.builder()
                        .fromUserId(promiseGoal.getUser().getId())
                        .fromUserNickname(promiseGoal.getUser().getNickname())
                        .promiseGoalId(promiseGoalId)
                        .build())
                .build();
        feedRepository.save(feed);
    }

    @Transactional(readOnly = true)
    public int countSuperDoneByPromiseGoalId(Long promiseGoalId) {
        // Count DONE
        return superDoneRepository.countByPromiseGoal_Id(promiseGoalId);
    }

    @Transactional(readOnly = true)
    public List<SuperDoneDto> getSuperDoneOfDayDetailList(Long userId, LocalDateTime createdAt) {
        //받은 UTC를 바탕으로 한국기준 오늘의 시작과 끝 설정
        LocalDateTime startOfDateKR = DateUtils.getLocalDateKSTfromUTC(createdAt).atStartOfDay();
        LocalDateTime endOfDateKR = startOfDateKR.plusDays(1);

        log.debug("startOfDateKR = " + startOfDateKR);
        log.debug("endOfDateKR = " + endOfDateKR);

        List<SuperDone> superDoneList = superDoneRepository.findByPromiseGoal_User_IdAndCreatedAtBetween(userId, startOfDateKR, endOfDateKR);
        return superDoneList.stream().map(SuperDoneDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<SuperDoneDto> getSuperDoneOfMonthList(Long userId, String yearMonth) {
        //yearMonth ("YYYY-MM")
        int year = Integer.parseInt(yearMonth.split("-")[0]);
        int month = Integer.parseInt(yearMonth.split("-")[1]);
        LocalDateTime ldtStartOfMonth = LocalDateTime.of(year, month, 1, 0, 0); //TODO 시간대 체크. ec2 시간대설정 이슈 발생시
        LocalDateTime ldtEndOfMonth = LocalDateTime.of(year, month + 1, 1, 0, 0);

        List<SuperDone> superDoneList = superDoneRepository.findByPromiseGoal_User_IdAndCreatedAtBetween(userId, ldtStartOfMonth, ldtEndOfMonth);
        return superDoneList.stream().map(SuperDoneDto::fromEntity).toList();
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
    private void throwIfNotDoneToday(String email, PromiseGoal promiseGoal) {
        findTodayDoneOfPromiseGoal(promiseGoal).orElseThrow(()
                -> new DailyoneException(ErrorCode.DONE_NOT_FOUND, String.format("email (%s) hasn't DONE today promise-goal of %d", email, promiseGoal.getId())));
    }
    private Optional<Done> findTodayDoneOfPromiseGoal(PromiseGoal promiseGoal) {
        //TODO : DoneService 의존성을 받아오지 않기 위해, 같은 코드지만 해당 Service에 이 메소드를 추가해둠.
        //       더 좋은 설계가 가능한지 알아보기
        //현재 날짜 가져오기
        LocalDate today = LocalDate.now();
        //오늘의 시작 시간 ( 00:00:00 )
        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
        //오늘의 종료 시간 ( 23:59:59 )
        Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX));

        return doneRepository.findByPromiseGoalAndCreatedAtBetween(promiseGoal, startOfDay, endOfDay);
    }

    //TODO 위 메소드와 상당히 유사한데 중복을 피할 수 있나?
    public Optional<SuperDone> findTodaySuperDoneByPromiseGoal(PromiseGoal promiseGoal) {
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();
        // 오늘의 시작 시간 ( 00:00:00 )
        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
        // 오늘의 종료 시간 ( 23:59:59 )
        Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX));

        return superDoneRepository.findByPromiseGoalAndCreatedAtBetween(promiseGoal, startOfDay, endOfDay);
    }
}

