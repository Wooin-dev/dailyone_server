package com.wooin.dailyone.service;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.dto.GoalDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@RequiredArgsConstructor
//@Transactional //TOSTUDY 클래스 레벨에 붙여주는 @Transactional의 동작 알아보기
@Service
public class GoalService { // cmd + shift + T : 테스트 생성 단축키

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final DoneRepository doneRepository;
    private final PromiseGoalRepository promiseGoalRepository;

    @Transactional
    public void create(GoalCreateRequest request, String email) {
        //user find
        User user = findUserByEmail(email);
        //goal save
        Goal goal = Goal.builderFromRequest(request).user(user).build();
        Goal savesGoal = goalRepository.save(goal);

        //생성이후 본인의 PromiseGoal로 등록
        PromiseGoal myPromiseGoal = PromiseGoal.builderFromRequest(request)
                                                .goal(savesGoal)
                                                .user(user)
                                                .build();
        promiseGoalRepository.save(myPromiseGoal);
    }

    @Transactional(readOnly = true)
    public GoalDto selectMyGoal(String email) {
        User user = findUserByEmail(email);
        Goal goal = goalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND));
        return GoalDto.fromEntity(goal);
    }

    @Transactional
    public void deleteGoal(String email) {
        User user = findUserByEmail(email);
        Goal goal = findMyGoalByUser(user);
        //Delete From DB
        goalRepository.delete(goal);
        //해당 유저의 해당 목표의 Done들을 삭제처리
        doneRepository.deleteByPromiseGoal_UserAndPromiseGoal_Goal(user, goal);
    }



    private Optional<Done> findDoneOfTodayByUserAndGoal(PromiseGoal promiseGoal) {
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();
        // 오늘의 시작 시간 ( 00:00:00 )
        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
        // 오늘의 종료 시간 ( 23:59:59 )
        Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX));

        return doneRepository.findByPromiseGoalAndCreatedAtBetween(promiseGoal, startOfDay, endOfDay);
    }

//    private void throwIfDoneToday(User user, Goal goal) {
//        Optional<Done> done = findDoneOfTodayByUserAndGoal(user, goal);
//        done.ifPresent((it) -> {
//            throw new DailyoneException(ErrorCode.ALREADY_DONE, String.format("email %s already DONE today goal of %d", user.getEmail(), goal.getId()));
//        });
//    }

    private Goal findGoalById(Long goalId) {
        return goalRepository.findById(goalId).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("Goal of %s is not found", goalId)));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s not found", email)));
    }

    private Goal findMyGoalByUser(User user) {
        return goalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("The goal of %s is not found", user)));
    }
}
