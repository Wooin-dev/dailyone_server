package com.wooin.dailyone.model;

import com.wooin.dailyone.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "users")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
@Table(indexes = {
        @Index(columnList = "email") //잦은 조회로 인한 인덱스 처리
}
)
public class User extends DefaultEntity {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private String password;

    @NotBlank
    @Size(min = 3, max = 15, message = "3이상 15이하 길이의 nickname을 입력해주세요.")
    private String nickname;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)//EnumType.ORDINAL : enum 순서 값을 DB에 저장. EnumType.STRING : enum 이름을 DB에 저장
    private UserRole role = UserRole.USER;

    @Column(name = "deleted_at") //소프트 삭제를 위한 필드
    private Timestamp deletedAt;

    @OrderBy("id")
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private final Set<Goal> goals = new LinkedHashSet<>();

    @Builder
    private User(String email, String password, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
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

    public void modifyMyInfo(UserDto requestDto) {
        this.nickname = requestDto.nickname();
    }
}
