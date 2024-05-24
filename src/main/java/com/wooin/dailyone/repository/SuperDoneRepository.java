package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.SuperDone;
import com.wooin.dailyone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SuperDoneRepository extends JpaRepository<SuperDone, Long> {
    int countByPromiseGoal_UserAndPromiseGoal_Goal(User user, Goal goal);
    List<SuperDone> findByPromiseGoal_UserAndCreatedAtBetween(User user, LocalDateTime startOfDateKR, LocalDateTime endOfDateKR);
    List<SuperDone> findByPromiseGoal_UserAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(User user, LocalDateTime ldtStartOfMonth, LocalDateTime ldtEndOfMonth);
    Optional<SuperDone> findByPromiseGoalAndCreatedAtBetween(PromiseGoal promiseGoal, Timestamp startOfDay, Timestamp endOfDay);
    int countByPromiseGoal(PromiseGoal promiseGoal);
    void deleteByPromiseGoal(PromiseGoal myPromiseGoal);
}
