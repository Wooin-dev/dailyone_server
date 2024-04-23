package com.wooin.dailyone.config;

import com.wooin.dailyone.config.filter.JwtTokenFilter;
import com.wooin.dailyone.exception.CustomAuthenticationEntryPoint;
import com.wooin.dailyone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    @Value("${jwt.secret-key}")
    private String key;
    private final UserService userService;
    private final CustomAuthenticationEntryPoint authenticationEntryPointHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF설정
        http.csrf(AbstractHttpConfigurer::disable);

        //인증 requestMatcher 설정
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/", "/api/*/users/join", "/api/*/users/login" ).permitAll()
                                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
                );

        //Session 설정 : 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        //필터 추가
        http.addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class);

        //필터에서 발생한 예외를 엔트리포인트를 지정해줘서 보내기.
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPointHandler));

        return http.build();
    }

}
