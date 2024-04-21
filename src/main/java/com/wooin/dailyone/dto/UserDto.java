package com.wooin.dailyone.dto;

import com.wooin.dailyone.model.User;
import com.wooin.dailyone.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.wooin.dailyone.model.User}
 */
public record UserDto(
        Long id,
        @Email @NotBlank String email,
        @NotNull String password,
        @Size(message = "3이상 15이하 길이의 nickname을 입력해주세요.", min = 3, max = 15) @NotBlank String nickname,
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