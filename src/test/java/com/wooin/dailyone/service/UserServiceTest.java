package com.wooin.dailyone.service;

import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입_정상_동작() {
    ////Given////WHEN
        String email = "wooin@test.com";
        String password = "pass12#$";
        String nickname = "wooin";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(mock(User.class));

    ////THEN
        Assertions.assertDoesNotThrow(() -> userService.join(email, password, nickname));
    }

    @Test
    void 회원가입시_email이_중복인_경우() {
        ////Given////WHEN
        String email = "wooin@test.com";
        String password = "pass12#$";
        String nickname = "wooin";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.save(any())).thenReturn(mock(User.class) );

        ////THEN
        DailyoneException e = Assertions.assertThrows(DailyoneException.class, () -> userService.join(email, password, nickname));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.DUPLICATED_EMAIL);
    }


    @Test
    void 로그인_정상_동작() {
        ////Given
        String email = "wooin@test.com";
        String password = "pass12#$";
        String encryptedPassword = "encryptedPassword";
        User joinedUser = User.of("wooin@test.com", encryptedPassword, "wooin");

        ////WHEN
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(joinedUser));
        when(encoder.matches(password, encryptedPassword)).thenReturn(true);

        ////THEN
        Assertions.assertDoesNotThrow(() -> userService.login(email, password));
    }

    @Test
    void 로그인시_email이_없는_경우() {
        ////Given////WHEN
        String email = "wooin@test.com";
        String password = "pass12#$";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ////THEN
        DailyoneException e = Assertions.assertThrows(DailyoneException.class, () -> userService.login(email, password));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.NOT_FOUND_EMAIL);
    }

    @Test
    void 로그인시_password가_틀린_경우() {
        ////Given////WHEN
        User joinedUser = User.of("wooin@test.com", "encrypted-password", "wooin");
        String email = "wooin@test.com";
        String wrongPass = "wrongPass";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(joinedUser));

        ////THEN
        DailyoneException e = Assertions.assertThrows(DailyoneException.class, () -> userService.login(email, wrongPass));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.INCORRECT_PASSWORD);
    }

}