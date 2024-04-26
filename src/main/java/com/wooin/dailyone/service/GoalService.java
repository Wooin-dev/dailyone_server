package com.wooin.dailyone.service;

import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.GoalRepository;
import com.wooin.dailyone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional //TOSTUDY 클래스 레벨에 붙여주는 @Transactional의 동작 알아보기
@Service
public class GoalService { // cmd + shift + T : 테스트 생성 단축키

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(GoalDto goalDto, String email) {
        //user find
        User user = findUserByEmailWithThrow(email);
        //goal save
        goalRepository.save(Goal.of(goalDto, user));
    }

    @Transactional(readOnly = true)
    public Goal selectMyGoal(String email) {
        User user = findUserByEmailWithThrow(email);
        Goal goal = goalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElse(null);
        return goal;
    }

    @Transactional
    public void deleteMyGoal(String email) {
        User user = findUserByEmailWithThrow(email);
        Goal goal = goalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("The goal of %s is not found", user)));
        //Delete From DB
        goalRepository.delete(goal);
    }



    private User findUserByEmailWithThrow(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s not found", email)));
    }
}
