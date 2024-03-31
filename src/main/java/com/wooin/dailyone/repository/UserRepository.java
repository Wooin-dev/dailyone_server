package com.wooin.dailyone.repository;

import com.wooin.dailyone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}