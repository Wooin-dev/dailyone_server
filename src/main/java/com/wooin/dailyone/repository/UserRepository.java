package com.wooin.dailyone.repository;

import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.wooin.dailyone.domain.QUser;
import com.wooin.dailyone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends
        JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>, // 기본적인 검색기능을 추가해준다
        QuerydslBinderCustomizer<QUser>
{
    @Override
    default void customize(QuerydslBindings bindings, QUser root) { //Java8 이후부터 인터페이스에서 바로 구현해줄수 있다.
        bindings.excludeUnlistedProperties(true); //모든 속성값에 대해 검색이 열려있을 텐데 이를 리스트에 있는 속성으로만 검색하게 설정. 기본값은 false
        bindings.including(root.id, root.email, root.nickname, root.phoneNum, root.username); // 해당 속성으로만 검색가능하게 설정
        //bindings.bind(root.nickname).first(StringExpression::likeIgnoreCase); // like '${v}'
        bindings.bind(root.id).first(SimpleExpression::eq);
        bindings.bind(root.email).first(StringExpression::containsIgnoreCase); //like '%${v}%'
        bindings.bind(root.nickname).first(StringExpression::containsIgnoreCase); //like '%${v}%'
        bindings.bind(root.phoneNum).first(StringExpression::containsIgnoreCase); //like '%${v}%'
        bindings.bind(root.username).first(StringExpression::containsIgnoreCase); //like '%${v}%'
    }
}