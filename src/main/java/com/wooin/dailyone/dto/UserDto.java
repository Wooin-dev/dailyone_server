package com.wooin.dailyone.dto;

import com.wooin.dailyone.controller.request.UserMyInfoUpdateRequest;
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
public record UserDto (
        Long id,
        String email,
        String password,
        String nickname,
        UserRole role,
        Timestamp deletedAt,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) implements UserDetails {

    //TODO : Request를 굳이 Dto로 변환할 필요가 있는지? 오히려 컨트롤러에서 서비스로 보낼때 용이하지 않을까? 다양한 타입을 담을 수 있으니까
    public static UserDto fromRequest(UserMyInfoUpdateRequest request) {
        return UserDto.builder()
                .nickname(request.getNickname())
                .build();
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getRole(),
                user.getDeletedAt(),
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
                .build();
    }


    //UserDetails 오버라이드 부분
    @Override
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
    public boolean isAccountNonExpired() {
        return this.deletedAt==null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.deletedAt==null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.deletedAt==null;
    }

    @Override
    public boolean isEnabled() {
        return this.deletedAt==null;
    }
}