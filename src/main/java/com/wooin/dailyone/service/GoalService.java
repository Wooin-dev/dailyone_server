package com.wooin.dailyone.service;

import com.wooin.dailyone.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional //TOSTUDY 클래스 레벨에 붙여주는 @Transactional의 동작 알아보기
@Service
public class GoalService { // cmd + shift + T : 테스트 생성 단축키

    private final GoalRepository goalRepository;

//    public GoalDto selectMyGoal(User user) {
//
//        return GoalDto.of();
//    }
}
