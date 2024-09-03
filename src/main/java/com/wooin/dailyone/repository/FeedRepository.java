package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.FeedOfGoal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedOfGoal, Long> {

    List<FeedOfGoal> findByGoal_IdOrderByCreatedAtDesc(Long id, Pageable pageable);

    FeedOfGoal findByGoal_Id(Long id);
}
