package com.wooin.dailyone.service;

import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.DoneRepository;
import com.wooin.dailyone.repository.GoalRepository;
import com.wooin.dailyone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional
    public void create(GoalDto goalDto, String email) {
        //user find
        User user = findUserByEmail(email);
        //TODO : 내 목표가 있는지 체크
        //goal save
        goalRepository.save(Goal.of(goalDto, user));
    }

    @Transactional(readOnly = true)
    public GoalDto selectMyGoal(String email) {
        User user = findUserByEmail(email);
        Goal goal = goalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElse(null);
        boolean isDoneToday = findDoneOfTodayByUserAndGoal(user, goal).isPresent();
        int doneCount = doneRepository.countByUserAndGoal(user, goal);
        return GoalDto.fromEntity(goal, isDoneToday, doneCount);
    }

    @Transactional
    public void deleteMyGoal(String email) {
        User user = findUserByEmail(email);
        Goal goal = findMyGoalByUser(user);
        //Delete From DB
        goalRepository.delete(goal);
    }

    @Transactional
    public void done(Long goalId, String email) {
        // User Exist
        User user = findUserByEmail(email);
        // Goal Exist
        Goal goal = findGoalById(goalId);
        // Check already DONE : 이미 오늘 DONE처리 되어있는지
        throwIfDoneToday(user, goal);
        // Save DONE
        doneRepository.save(new Done(user, goal));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int doneCountByGoalIdAndEmail(Long goalId, String email) {
        // User Exist
        User user = findUserByEmail(email);
        // Goal Exist
        Goal goal = findGoalById(goalId);
        // Count DONE
        return doneRepository.countByUserAndGoal(user, goal);
    }


    private Optional<Done> findDoneOfTodayByUserAndGoal(User user, Goal goal) {
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();
        // 오늘의 시작 시간 ( 00:00:00 )
        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
        // 오늘의 종료 시간 ( 23:59:59 )
        Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX));

        return doneRepository.findByUserAndGoalAndCreatedAtBetween(user, goal, startOfDay, endOfDay);
    }
    private void throwIfDoneToday(User user, Goal goal) {
        Optional<Done> done = findDoneOfTodayByUserAndGoal(user, goal);
        done.ifPresent((it) -> {
            throw new DailyoneException(ErrorCode.ALREADY_DONE, String.format("email %s already DONE today goal of %d", user.getEmail(), goal.getId()));
        });
    }
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
