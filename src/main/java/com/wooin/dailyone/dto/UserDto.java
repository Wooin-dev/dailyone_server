package com.wooin.dailyone.dto;

import com.wooin.dailyone.model.User;
import com.wooin.dailyone.model.UserRole;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.wooin.dailyone.model.User}
 */
public record UserDto(
        Long id,
        @Email String email,
        String password,
        String nickname,
        UserRole role,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt(),
                user.getCreatedBy(),
                user.getModifiedAt(),
                user.getModifiedBy());
    }

}