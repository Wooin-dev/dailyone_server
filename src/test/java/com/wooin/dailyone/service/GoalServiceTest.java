package com.wooin.dailyone.service;

import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.GoalRepository;
import com.wooin.dailyone.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("비즈니스 로직 - Goal")
@ExtendWith(MockitoExtension.class) //스프링부트 애플리케이션 컨텍스트 로딩시간을 줄이고자 함 -> 모킹을 사용 (모키토)
class GoalServiceTest {

    // @InjectMocks : 주입 대상 객체를 나타내며 해당 객체에 필요한 의존성을 자동으로 주입하는 데 사용
    // @Mock : 주입 대상 객체가 의존하는 다른 객체들의 모의 객체를 생성하는 데 사용
    @InjectMocks private GoalService sut; // system under test (SUT) : 테스트하고자 하는 주요 대상
    @Mock private GoalRepository goalRepository;
    @Mock private UserRepository userRepository;

    @DisplayName("나의 Goal 조회")
    @Test
    void givenUserParameter_whenSearchingGoal_thenReturnMyGoal() {
        ////Given
        User user = userRepository.findById(1L).orElseThrow();

//        ////WHEN
//        GoalDto goalDto = sut.selectMyGoal(user);
//
//        ////THEN
//        assertThat(goalDto).isNotNull();
    }
}