package com.wooin.everyfive.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends DefaultEntity {

    @NotBlank (message = "id는 필수입력 값입니다.")
    @Size(min = 4, max = 20, message = "4이상 20이하 길이의 id를 입력해주세요.")
    private String id;

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


    @OneToOne
    private Goal goal;

}
