package com.wooin.dailyone.service;

import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.UserRepository;
import com.wooin.dailyone.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    //UserDetailService 인터페이스의 loadUserByUsername을 사용하지 않고 커스텀하는 느낌으로 직접 구현
    public UserDto loadUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserDto::from).orElseThrow(()->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s is not found", email)));
    }

    @Transactional //트랜잭션으로 묶어줌으로써 중간에 예외가 나면 자동 롤백된다.
    public UserDto join(String email, String password, String nickname) {
        // 중복여부 체크
        // 기존 데이터가 존재할 시 예외를 throw하는 것으로 중복체크 가능
        userRepository.findByEmail(email).ifPresent(e -> {
            throw new DailyoneException(ErrorCode.DUPLICATED_EMAIL, String.format("%s is duplicated", email));
        });

        // 회원가입 동작 -> user DB에 등록
        String encryptedPassword = encoder.encode(password);
        User savedUser = userRepository.save(User.of(email, encryptedPassword, nickname));
        return UserDto.from(savedUser);
    }

    public String login(String email, String password) {
        //가입된 회원인지 여부 체크
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s is not joined", email)));

        //비밀번호 체크
        if(!encoder.matches(password, user.getPassword())){
            throw new DailyoneException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰생성
        String token = JwtTokenUtils.generateToken(email, secretKey, expiredTimeMs);

        return token;
    }

}
