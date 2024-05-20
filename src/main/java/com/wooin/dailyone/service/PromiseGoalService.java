package com.wooin.dailyone.service;

import com.wooin.dailyone.controller.request.PromiseGoalCreateRequest;
import com.wooin.dailyone.controller.response.promisegoal.MyPromiseGoalResponse;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.dto.PromiseGoalDto;
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
@Service
public class PromiseGoalService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final PromiseGoalRepository promiseGoalRepository;
    private final DoneRepository doneRepository;


    @Transactional
    public void createPromiseGoal(PromiseGoalCreateRequest request, String email) {
        User user = findUserByEmail(email);
        Goal goal = findGoalById(request.getGoalId());
        PromiseGoal createPromiseGoal = PromiseGoal.builder()
                .user(user)
                .goal(goal)
                .promiseDoneCount(request.getPromiseDoneCount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        promiseGoalRepository.save(createPromiseGoal);
    }

    @Transactional(readOnly = true)
    public MyPromiseGoalResponse selectMyPromiseGoal(String email) {
        User user = findUserByEmail(email);
        PromiseGoal promiseGoal = promiseGoalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElseThrow(()->
                new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FOUND));
        int doneCount = doneRepository.countByPromiseGoal(promiseGoal);
        boolean isDoneToday = findDoneOfTodayByUserAndGoal(promiseGoal).isPresent();

        return new MyPromiseGoalResponse(
                GoalDto.fromEntity(promiseGoal.getGoal()),
                PromiseGoalDto.fromEntity(promiseGoal),
                doneCount,
                isDoneToday);
    }

    @Transactional
    public void deleteMyPromiseGoal(String email) {
        PromiseGoal myPromiseGoal = findPromiseGoalByEmail(email); //TODO: 현재는 단건조회(PromiseGoal을 하나만 갖는다는 전제). 추후 다건 조회가능할때 그에맞게 변경 필요
        //연관된 DONE 삭제
        doneRepository.deleteByPromiseGoal(myPromiseGoal);
        //PromiseGoal 삭제
        promiseGoalRepository.delete(myPromiseGoal);
        //TODO: Goal또한 같이 삭제할지 여부
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

    private Goal findGoalById(Long goalId) {
        return goalRepository.findById(goalId).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("Goal of %s is not found", goalId)));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s not found", email)));
    }

    private PromiseGoal findPromiseGoalByEmail(String email) {
        User user = findUserByEmail(email);
        return promiseGoalRepository.findByUser(user).orElseThrow(() ->
                new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FOUND, String.format("PromiseGoal of %s is not found", email)));

    }
}
