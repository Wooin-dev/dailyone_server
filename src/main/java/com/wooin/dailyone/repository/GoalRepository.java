package com.wooin.dailyone.repository;

import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.QGoal;
import com.wooin.dailyone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface GoalRepository extends
        JpaRepository<Goal, Long>,
        QuerydslPredicateExecutor<Goal>, // 기본적인 검색기능을 추가해준다
        QuerydslBinderCustomizer<QGoal> {
    @Override
    default void customize(QuerydslBindings bindings, QGoal root) { //Java8 이후부터 인터페이스에서 바로 구현해줄수 있다.
        bindings.excludeUnlistedProperties(true); //모든 속성값에 대해 검색이 열려있을 텐데 이를 리스트에 있는 속성으로만 검색하게 설정. 기본값은 false
        bindings.including(root.id, root.originalGoal, root.simpleGoal, root.motivationComment, root.congratsComment); // 해당 속성으로만 검색가능하게 설정
        bindings.bind(root.id).first(SimpleExpression::eq);
        bindings.bind(root.originalGoal).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.simpleGoal).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.motivationComment).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.congratsComment).first(StringExpression::containsIgnoreCase);
    }

    Optional<Goal> findByUser(User user);

    Optional<Goal> findFirstByUserOrderByCreatedAtDesc(User user);
}