package com.wooin.dailyone.dto;

import com.wooin.dailyone.model.User;
import com.wooin.dailyone.model.UserRole;
import jakarta.validation.constraints.Email;
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

    public static UserDto from(User user) {
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