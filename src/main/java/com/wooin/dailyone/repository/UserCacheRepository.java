package com.wooin.dailyone.repository;

import com.wooin.dailyone.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, UserDto> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(UserDto user) {
        String key = getKey(user.email());
        log.info("Set User to Redis {} - {}", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<UserDto> getUser(String email) {
        String key = getKey(email);
        UserDto user = userRedisTemplate.opsForValue().get(key);
        log.info("Get User from Redis {} - {}", key, user);
        return Optional.ofNullable(user);
    }

    private String getKey(String email) { //인증시 email값으로 select가 이루어진다. 여기에 캐싱을 적용하기 용이하게 key를 email로 설정.
        //prefix를 붙여주는 것이 추후 캐싱데이터가 여러 종류가 됐을때 구별하기 쉽게 도와준다.
        return "USER:" + email;
    }

}
