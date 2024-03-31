package com.wooin.dailyone.repository;

import com.wooin.dailyone.config.JpaConfig;
import com.wooin.dailyone.domain.Goal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public JpaRepositoryTest(@Autowired GoalRepository goalRepository,
                             @Autowired UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    @DisplayName("Goal Select")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        ////WHEN
        List<Goal> goals = goalRepository.findAll();

        ////THEN
        assertThat(goals)
                .isNotNull()
                .hasSize(0);
    }

    @DisplayName("Goal insert")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        ////GIVEN
        long previousCount = goalRepository.count();

        ////WHEN
        Goal savedGoal = goalRepository.save(
                Goal.of("test Goal",
                        "Goal",
                        "moti",
                        "congrats"));
        ////THEN
        assertThat(goalRepository.count())
                .isEqualTo(previousCount + 1);
        assertThat(savedGoal.getOriginalGoal())
                .isNotNull()
                .isEqualTo("test Goal");
    }
}