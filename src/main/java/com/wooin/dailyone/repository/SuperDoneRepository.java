package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.SuperDone;
import com.wooin.dailyone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SuperDoneRepository extends JpaRepository<SuperDone, Long> {
    List<SuperDone> findByPromiseGoal_User_IdAndCreatedAtBetween(Long userId, LocalDateTime startOfDateKR, LocalDateTime endOfDateKR);
    List<SuperDone> findByPromiseGoal_UserAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(User user, LocalDateTime ldtStartOfMonth, LocalDateTime ldtEndOfMonth);
    Optional<SuperDone> findByPromiseGoalAndCreatedAtBetween(PromiseGoal promiseGoal, Timestamp startOfDay, Timestamp endOfDay);
    int countByPromiseGoal(PromiseGoal promiseGoal);
    int countByPromiseGoal_Id(Long promiseGoalId);

    @Modifying
    @Query("UPDATE SuperDone sd SET sd.deletedAt = NOW() where sd.promiseGoal = :promiseGoal")
    void deleteByPromiseGoal(@Param("promiseGoal") PromiseGoal myPromiseGoal);
}
