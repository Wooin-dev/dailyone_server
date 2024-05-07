package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoneRepository extends JpaRepository<Done, Long> {
    Optional<Done> findByUserAndGoalAndCreatedAtBetween(User user, Goal goal, Timestamp createdAtStart, Timestamp createdAtEnd);

    int countByUserAndGoal(User user, Goal goal);

    List<Done> findByUserAndCreatedAtBetween(User user, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    List<Done> findByUserAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(User user, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);
}