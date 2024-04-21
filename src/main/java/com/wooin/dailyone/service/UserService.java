package com.wooin.dailyone.service;

import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(String email, String password, String nickname) {
        // 중복여부 체크
        // 기존 데이터가 존재할 시 예외를 throw하는 것으로 중복체크 가능
        userRepository.findByEmail(email).ifPresent(e -> {
            throw new DailyoneException(ErrorCode.DUPLICATED_EMAIL, String.format("%s is duplicated", email));
        });

        // 회원가입 동작 -> user DB에 등록
        User savedUser = userRepository.save(User.of(email, password, nickname));
        return UserDto.from(savedUser);
    }

    //TODO : implement
    public String login(String email, String password) {
        //가입된 회원인지 여부 체크
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new DailyoneException(ErrorCode.NOT_JOINED_EMAIL, String.format("%s is not joined", email)));

        //비밀번호 체크  TODO : 암호화 추가
        if (!user.getPassword().equals(password)) {
            throw new DailyoneException(ErrorCode.INCORRECT_PASSWORD, "Incorrect Password");
        }

        //토큰생성

        return "";
    }

}
