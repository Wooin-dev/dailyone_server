package com.wooin.dailyone.service;

import com.wooin.dailyone.controller.request.UserMyInfoUpdateRequest;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.model.UserRole;
import com.wooin.dailyone.repository.UserCacheRepository;
import com.wooin.dailyone.repository.UserRepository;
import com.wooin.dailyone.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms.user}")
    private Long expiredTimeMs;

    //UserDetailService 인터페이스의 loadUserByUsername을 사용하지 않고 커스텀하는 느낌으로 직접 구현
    @Transactional(readOnly = true)
    public UserDto loadUserByEmail(String email) {
        return userCacheRepository.getUser(email).orElseGet(() ->
         userRepository.findByEmail(email).map(UserDto::fromEntity).orElseThrow(()->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s is not found", email)))
        );
    }

    @Transactional //트랜잭션으로 묶어줌으로써 중간에 예외가 나면 자동 롤백된다.
    public UserDto join(String email, String password, String nickname) {
        // 중복여부 체크
        // 기존 데이터가 존재할 시 예외를 throw하는 것으로 중복체크 가능
        userRepository.findByEmail(email).ifPresent(e -> {
            throw new DailyoneException(ErrorCode.DUPLICATED_EMAIL, String.format("%s is duplicated", email));
        });

        // 회원가입 동작 ->
        // 암호화
        String encryptedPassword = encoder.encode(password);
        // 권한설정
        UserRole role = UserRole.USER; //TODO: 권한 설정 분기추가
        //user객체 생성 및 DB에 등록
        User newUser = User.builder().email(email).password(encryptedPassword).nickname(nickname).role(role).build();
        User savedUser = userRepository.save(newUser);
        return UserDto.fromEntity(savedUser);
    }

    public String login(String email, String password) {
        //가입된 회원인지 여부 체크
        UserDto userDto = loadUserByEmail(email);

        //캐싱데이터set
        userCacheRepository.setUser(userDto);

        //비밀번호 체크
        if(!encoder.matches(password, userDto.password())){
            throw new DailyoneException(ErrorCode.INVALID_PASSWORD);
        }
        //토큰생성
        String token = JwtTokenUtils.generateToken(email, secretKey, expiredTimeMs);

        return token;
    }

    @Transactional(readOnly = true)
    public Boolean checkEmailDuplicated(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    @Transactional
    public UserDto modifyMyInfo(UserMyInfoUpdateRequest requestDto, UserDto userDto) {
        //user exist
        User user = userRepository.findById(userDto.id()).orElseThrow(()
                -> new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s is not found", userDto.email())));
        //modify & save
        user.modifyMyInfo(requestDto);
        User modifiedUser = userRepository.save(user); //TOSTUDY JpaRepository에서 save메소드가 리턴하는 엔티티의 출처. flush 타이밍과 연계하여
        //트랜잭션을 통해 save는 자동으로 일어나지만 이후 ORM이나 DB의 변경 후,
        //더티체킹을 지원하지 않을 시에 오류가 발생할 수 있다.
        //개방폐쇄원칙에 맞지 않다. //TOSTUDY 개방폐쇄원칙

        //캐싱데이터 업데이트
        userCacheRepository.setUser(UserDto.fromEntity(modifiedUser));

        userRepository.flush();
        // flush가 없는 경우 엔티티에 직접 수정이 이뤄진 사항은 반영이 되지만, modifiedAt과 같은 변경사항이 반영이 되지 않는다.

        return UserDto.fromEntity(modifiedUser);
    }
}
