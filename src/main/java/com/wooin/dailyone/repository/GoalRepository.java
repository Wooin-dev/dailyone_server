package com.wooin.dailyone.repository;

import com.wooin.dailyone.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}