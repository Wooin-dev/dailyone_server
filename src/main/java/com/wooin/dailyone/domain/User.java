package com.wooin.dailyone.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "users")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
                @Index(columnList = "username")
        }
)
public class User extends DefaultEntity {

    @NotBlank (message = "username는 필수입력 값입니다.")
    @Size(min = 4, max = 20, message = "4이상 20이하 길이의 id를 입력해주세요.")
    private String username;

    @NotNull
    private String password;

    @NotBlank
    @Size(min = 4, max = 15, message = "4이상 15이하 길이의 nickname을 입력해주세요.")
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^01([0|1|6|7|8|9])?([0-9]{3,4})?([0-9]{4})$")
    private String phoneNum;

    @NotBlank
    @Email
    private String email;

    @OrderBy("id")
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private final Set<Goal> goals = new LinkedHashSet<>();

    private User(String username, @NotNull String password, String nickname, String phoneNum, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public static User of(String username, String password, String nickname, String phoneNum, String email) {
        return new User(username, password, nickname, phoneNum, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return user != null && Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
