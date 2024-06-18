package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.PromiseGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoneRepository extends JpaRepository<Done, Long> {
    Optional<Done> findByPromiseGoalAndCreatedAtBetween(PromiseGoal promiseGoal, Timestamp createdAtStart, Timestamp createdAtEnd);
    List<Done> findByPromiseGoal_User_IdAndCreatedAtBetween(Long id, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    @Modifying
    @Query("UPDATE Done d SET d.deletedAt = NOW() where d.promiseGoal = :promiseGoal")
    void deleteByPromiseGoal(@Param("promiseGoal") PromiseGoal promiseGoal);
    int countByPromiseGoal(PromiseGoal promiseGoal);
    int countByPromiseGoal_Id(Long promiseGoalId);
}
