package com.wooin.dailyone.service;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.DoneRepository;
import com.wooin.dailyone.repository.GoalRepository;
import com.wooin.dailyone.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class GoalServiceTest {

    @Autowired
    private GoalService goalService;

    @MockBean //테스트 대상이 아니지만 Bean은 필요해
    private GoalRepository goalRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DoneRepository doneRepository;

    @Test
    void 목표생성_성공() {
        //GIVEN
        String email = "wooin@test.com";

        String originalGoal = "푸시업 매일 10개";
        String simpleGoal = "푸시업 매일 1개";
        String motivationComment = "화이팅";
        String congratsComment = "축하";

        GoalCreateRequest request = new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment);
        GoalDto goalDto = GoalDto.fromRequest(request);

        //MOCKING
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(goalRepository.save(any())).thenReturn(mock(Goal.class));

        //WHEN//THEN
        Assertions.assertDoesNotThrow(() -> goalService.create(goalDto, email));

    }

    @Test
    void 목표생성시_요청한유저가_존재하지않는경우() {
        //GIVEN
        String email = "wooin@test.com";

        //MOCKING
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(goalRepository.save(any())).thenReturn(mock(Goal.class));

        //WHEN//THEN
        DailyoneException e = Assertions.assertThrows(DailyoneException.class, () -> goalService.create(mock(GoalDto.class), email));
        Assertions.assertEquals(ErrorCode.EMAIL_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 내목표_조회_성공() {
        //GIVEN
        String email = "wooin@test.com";
        //MOCKING
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(goalRepository.findFirstByUserOrderByCreatedAtDesc(any())).thenReturn(Optional.of(mock(Goal.class)));
        when(doneRepository.findByUserAndGoalAndCreatedAtBetween(any(), any(), any(), any())).thenReturn(Optional.empty());
        when(doneRepository.countByUserAndGoal(any(), any())).thenReturn(1);

        //WHEN//THEN
        Assertions.assertDoesNotThrow(() -> goalService.selectMyGoal(email));
    }

    @Test
    void 목표삭제_성공() {
        //GIVEN
        String email = "wooin@test.com";
        //MOCKING
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(goalRepository.findFirstByUserOrderByCreatedAtDesc(any())).thenReturn(Optional.of(mock(Goal.class)));
        //WHEN//THEN
        Assertions.assertDoesNotThrow(() -> goalService.deleteMyGoal(email));
    }

    @Test
    void 목표삭제시_목표가_존재하지않는경우() {
        //GIVEN
        String email = "wooin@test.com";
        //MOCKING
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(goalRepository.findFirstByUserOrderByCreatedAtDesc(any())).thenReturn(Optional.empty());
        //WHEN//THEN
        DailyoneException e = Assertions.assertThrows(DailyoneException.class, () -> goalService.deleteMyGoal(email));
        Assertions.assertEquals(ErrorCode.GOAL_NOT_FOUND, e.getErrorCode());
    }

}