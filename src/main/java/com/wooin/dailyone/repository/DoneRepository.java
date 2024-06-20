package com.wooin.dailyone.repository;

import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.PromiseGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoneRepository extends JpaRepository<Done, Long> {
    Optional<Done> findByPromiseGoalAndCreatedAtBetween(PromiseGoal promiseGoal, Timestamp createdAtStart, Timestamp createdAtEnd);
    List<Done> findByPromiseGoal_User_IdAndCreatedAtBetween(Long id, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    @Transactional //TOSTUDY 여기에 붙는 Transactional의 의미. (part08. ch04. 02. 31:02경) 여러개가 삭제되다 중간에 오류나면 롤백하기 위함인가.?
    @Modifying
    @Query("UPDATE Done d SET d.deletedAt = NOW() where d.promiseGoal = :promiseGoal")
    void deleteByPromiseGoal(@Param("promiseGoal") PromiseGoal promiseGoal);
    int countByPromiseGoal(PromiseGoal promiseGoal);
    int countByPromiseGoal_Id(Long promiseGoalId);
}
