package com.wooin.dailyone.repository;

import com.wooin.dailyone.config.JpaConfig;
import com.wooin.dailyone.domain.Goal;
import com.wooin.dailyone.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @DisplayName("Goal Select all")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        ////WHEN
        List<Goal> goals = goalRepository.findAll();

        ////THEN
        assertThat(goals)
                .isNotNull()
                .hasSize(300);
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


    @DisplayName("Goal update")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        ////GIVEN
        Goal selectedGoal = goalRepository.findById(1L).orElseThrow();
        String updatedOriginalGoal = "updated Goal";
        selectedGoal.setOriginalGoal(updatedOriginalGoal);

        ////WHEN
        Goal savedGoal = goalRepository.saveAndFlush(selectedGoal); //TIL : 이 부분은 Flush 해주지않으면 결국 롤백될 부분이라서 하이버네이트가 업데이트 쿼리를 발생시키지 않는다.

        ////THEN
        assertThat(savedGoal).hasFieldOrPropertyWithValue("originalGoal", updatedOriginalGoal);

    }


    @DisplayName("Goal update")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        ////GIVEN
        Goal selectedGoal = goalRepository.findById(1L).orElseThrow();
        long previousGoalCount = goalRepository.count();

        ////WHEN
        goalRepository.delete(selectedGoal);

        ////THEN
        assertThat(goalRepository.count()).isEqualTo(previousGoalCount -1);

    }










    @DisplayName("User Select All")
    @Test
    void user_givenTestData_whenSelecting_thenWorksFine() {
        ////WHEN
        List<User> users = userRepository.findAll();

        ////THEN
        assertThat(users)
                .isNotNull()
                .hasSize(300);
    }


}