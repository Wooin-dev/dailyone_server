package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface PromiseGoalRepository extends
        JpaRepository<PromiseGoal, Long> {

    Optional<PromiseGoal> findFirstByUserOrderByCreatedAtDesc(User user);
    Optional<PromiseGoal> findByUser(User user);
    List<PromiseGoal> findByUserOrderByCreatedAtDesc(User user);

    @Override
    @Modifying
    @Query("UPDATE PromiseGoal SET deletedAt = NOW() where id = :promiseGoalId")
    void deleteById(@Param("promiseGoalId") Long promiseGoalId);

    int countByGoal_Id(Long id);

    Optional<PromiseGoal> findFirstByUserAndGoal(User user, Goal goal);
}
