package com.wooin.dailyone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooin.dailyone.controller.response.user.UserLoginResponse;
import com.wooin.dailyone.dto.KakaoUserDto;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.model.UserRole;
import com.wooin.dailyone.repository.UserCacheRepository;
import com.wooin.dailyone.repository.UserRepository;
import com.wooin.dailyone.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j(topic = "KAKAO Login")
@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder encoder;

    @Value("${spring.base-uri}")
    private String BASE_URI;
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Transactional
    public UserLoginResponse kakaoLogin(String code) throws JsonProcessingException { //String code는 카카오로부터 받은 인가 코드
        // 1. "인가 코드"로 "액세스 토큰" 요청. 액세스 토큰은 카카오 사용자 정보를 가져오기 위한 코드
        String accessToken = getToken(code);
        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserDto kakaoUserDto = getKakaoUserInfo(accessToken);
        // 3. 필요시 회원가입 아니라면 바로 user가져오기.
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserDto);
        // 4. JWT 토큰 생성
        String token = JwtTokenUtils.generateToken(kakaoUser.getEmail(), secretKey);
        // 5. User캐시 저장
        userCacheRepository.setUser(UserDto.fromEntity(kakaoUser));

        return new UserLoginResponse(token);
    }


    private String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "7f4f0eb4fb9ca5dc05eb0f926eaf7e03");
        body.add("redirect_uri", BASE_URI + "/kakao-login");
        body.add("code", code);

        log.info(BASE_URI + " : base uri ####");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri) // POST방식
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 만들기
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기 // 여기서 응답을 받아옴.
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        log.debug("jsonNode = " + jsonNode.toString());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
//        String email;
//        try {
//            email = jsonNode.get("kakao_account").get("email").asText();
//        } catch (NullPointerException e) {
//            throw new IllegalArgumentException("이메일 정보를 가져올 수 없습니다.");
//        }
        log.info("카카오 사용자 정보: " + id + ", " + nickname);
        return new KakaoUserDto(id, nickname);
    }

    private User registerKakaoUserIfNeeded(KakaoUserDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.id();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        // 카카오로 가입된 회원이 있다면 바로 리턴 TODO : 추후 이메일을 받아올 수 있게되면 해당 User에 받아온 이메일을 등록하는 과정 추가
        if (kakaoUser != null) return kakaoUser;

        // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인 //TODO 카카오 사용자정보에서 이메일을 가져올 수 있을 때 수정
//        Optional<User> foundUser = userRepository.findByEmail(kakaoUserInfo.email());
//        if (foundUser.isEmpty()) {
            // 신규 회원가입
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = encoder.encode(password);
            // email: kakao email
//            String email = kakaoUserInfo.email(); TODO email을  받아올 수 있게되면 받아온 이메일로 등록
            String nickname = kakaoUserInfo.nickname();

            User newUser = User.builder()
                    .password(encodedPassword)
                    .email("kakao"+kakaoId+"@kakaoTemp.com") //TODO : 현재는 임시 이메일 주소 생성. 추후 변경
                    .nickname(nickname)
                    .role(UserRole.USER)
                    .kakaoId(kakaoId).build();
            kakaoUser = userRepository.save(newUser);
//        } else {
            // 기존 회원정보에 카카오 Id 추가
//            kakaoUser = foundUser.get().kakaoIdUpdate(kakaoId);
//        }
        return kakaoUser;
    }
}
