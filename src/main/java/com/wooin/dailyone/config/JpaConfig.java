package com.wooin.dailyone.config;

import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.util.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(principal -> {
                    if (principal instanceof UserDto) {
                        UserDto userDto = ClassUtils.getSafeCastInstance(principal, UserDto.class);
                        return userDto.getUsername() != null ? userDto.getUsername() : "NoAuth" ;
                    } else {
                        return "NoAuth"; // 또는 다른 특정한 String 값
                    }
                });
    }
}
