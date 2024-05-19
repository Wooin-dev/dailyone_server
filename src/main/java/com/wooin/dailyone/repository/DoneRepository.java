package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoneRepository extends JpaRepository<Done, Long> {
    Optional<Done> findByPromiseGoalAndCreatedAtBetween(PromiseGoal promiseGoal, Timestamp createdAtStart, Timestamp createdAtEnd);
    List<Done> findByPromiseGoal_UserAndCreatedAtBetween(User user, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);
    List<Done> findByPromiseGoal_UserAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(User user, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);
    List<Done> deleteByPromiseGoal_UserAndPromiseGoal_Goal(User user, Goal goal);
    int countByPromiseGoal(PromiseGoal promiseGoal);
    int countByPromiseGoal_UserAndPromiseGoal_Goal(User user, Goal goal);

    List<Done> deleteByPromiseGoal(PromiseGoal promiseGoal);
}