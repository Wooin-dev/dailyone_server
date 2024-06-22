package com.wooin.dailyone.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.model.UserRole;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * DTO for {@link com.wooin.dailyone.model.User}
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) //UserDetails의 속성들은 unknown되어있고 이는 무시하기 위한 어노테이션
public record UserDto (
        Long id,
        String email,
        String password,
        String nickname,
        UserRole role,
        Timestamp deletedAt,
        Long kakaoId,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime createdAt,
        String createdBy,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime modifiedAt,
        String modifiedBy
) implements UserDetails {

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getRole(),
                user.getDeletedAt(),
                user.getKakaoId(),
                user.getCreatedAt(),
                user.getCreatedBy(),
                user.getModifiedAt(),
                user.getModifiedBy());
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(role)
                .kakaoId(kakaoId)
                .build();
    }


    //UserDetails 오버라이드 부분
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((this.role().toString())));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return this.deletedAt==null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return this.deletedAt==null;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return this.deletedAt==null;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.deletedAt==null;
    }
}
