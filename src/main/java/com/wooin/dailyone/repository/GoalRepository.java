package com.wooin.dailyone.repository;

import com.wooin.dailyone.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GoalRepository extends JpaRepository<Goal, Long> {
}