package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface PromiseGoalRepository extends
        JpaRepository<PromiseGoal, Long> {

    Optional<PromiseGoal> findFirstByUserOrderByCreatedAtDesc(User user);
    Optional<PromiseGoal> findByUser(User user);
}