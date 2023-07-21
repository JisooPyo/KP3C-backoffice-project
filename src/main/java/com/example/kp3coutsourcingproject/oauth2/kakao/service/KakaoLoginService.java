package com.example.kp3coutsourcingproject.oauth2.kakao.service;

import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.oauth2.kakao.dto.KakaoUserInfoDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoLoginService {
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void kakaoSocialLogin(String code, String registrationId) {
        String accessToken = kakaoGetAccessToken(code, registrationId);
        JsonNode userResourceNode = kakaoGetUserResource(accessToken, registrationId);
        System.out.println("userResourceNode = " + userResourceNode);

        String kakaoId = userResourceNode.get("id").asText();
        String kakaoEmail = userResourceNode.get("kakao_account").get("email").asText();
        String kakaoNickname = userResourceNode.get("properties").get("nickname").asText();
        System.out.println("id = " + kakaoId);
        System.out.println("Email = " + kakaoEmail);
        System.out.println("Nickname = " + kakaoNickname);
    }

    private String kakaoGetAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode kakaoGetUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("oauth2." + registrationId + ".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

//    public void kakaoLogin(String code, String registraionId) throws JsonProcessingException {
//        String accessToken = kakaoGetAccessToken(code, registraionId);
//
//        KakaoUserInfoDto kakaoUserInfo = kakaoGetUserResource(accessToken);
//
//        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
//
//        forceLogin(kakaoUser);
//    }

//    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
//        //db에 중복 id확인
//
//        Long kakaoId = kakaoUserInfo.getId();
//        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);
//        if (kakaoUser == null) {
//            String kakaoEmail = kakaoUserInfo.getEmail();
//            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
//            if (sameEmailUser != null) {
//                kakaoUser = sameEmailUser;
//                kakaoUser.setKakaoId(kakaoId);
//            }else {
//                //신규
//                String nickname = kakaoUserInfo.getNickname();
//
//                String password = UUID.randomUUID().toString();
//                String encodedPassword = passwordEncoder.encode(password);
//
//                String email = kakaoUserInfo.getEmail();
//
//                UserRoleEnum role = UserRoleEnum.USER;
//
//                kakaoUser = new User(nickname, encodedPassword, email, role, kakaoId);
//            }
//
//            userRepository.save(kakaoUser);
//
//            }
//        return kakaoUser;
//        }
//
//        private void forceLogin(User kakaoUser) {
//            UserDetails userDetails = new UserDetailsImpl(kakaoUser);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//}

//    // 주입받을 것들
////    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//    private final RestTemplate restTemplate;
//    private final JwtUtil jwtUtil;
//
//    public String kakaoLogin(String code) throws JsonProcessingException {
//        //1. 인가 코드로 액세스 토큰 요청
//        String accessToken = getToken(code);
//        //2. 토큰으로 카카오api호출 : "액세스토큰"으로 "카카오 사용자 정보"가저오기
//        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
//        return null;
//    }
//
//    public String getToken(String code) throws JsonProcessingException {
//        // 요청 URL 만들기
//        URI uri = UriComponentsBuilder
//                .fromUriString("http://kauth.kakao.com")
//                .path("oauth/token")
//                .encode()
//                .build()
//                .toUri();
//
//        //HTTP header 생성
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencode;charset=utf-8");
//
//        //HTTP Body 생성
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", "7455eac9bc76a0c055254eea8bc6bb38"); //7455eac9bc76a0c055254eea8bc6bb38 입력
//        body.add("redirect_uri", "http://localhost:8080/kp3c/user/kakao/callback");
//        body.add("code", code);
//
//        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri)
//                .headers(headers)
//                .body(body);
//
//        // HTTP 요청 보내기
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                requestEntity, String.class);
//
//        // HTTP 응답 (Json) -> 액세스 토큰 파싱
//
//        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
//        return jsonNode.get("access_token").asText();
//    }
//
//    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
//        //요청 URL 만들기
//        URI uri = UriComponentsBuilder
//                .fromUriString("http://kapi.kakao.com")
//                .path("/v2/user/me")
//                .encode()
//                .build()
//                .toUri();
//
//        //HTTP Header 생성
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencode;charset=utf-8");
//
//        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
//                .post(uri)
//                .headers(headers)
//                .body(new LinkedMultiValueMap<>());
//
//        // HTTP 요청 보내기
//        ResponseEntity<String> response = restTemplate.exchange(
//                requestEntity, String.class);
//
//        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
//        Long id = jsonNode.get("id").asLong();
//        String nickname = jsonNode.get("properties")
//                .get("nickname").asText();
//        String email = jsonNode.get("kakao_acoount")
//                .get("email").asText();
//
//        log.info("카카오 사용자 정보 " + id + ", " + nickname + " , " + email);
//        return new KakaoUserInfoDto(id, nickname, email);
//
//
//    }
}
