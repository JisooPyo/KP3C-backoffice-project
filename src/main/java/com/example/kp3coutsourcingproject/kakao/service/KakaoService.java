package com.example.kp3coutsourcingproject.kakao.service;

import com.example.kp3coutsourcingproject.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.kakao.dto.KakaoUserInfoDto;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpHeaders;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

     /*주입받을 것들*/
    //private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    /*사용자가 서비스에서 카카오 로그인 버튼을 클릭하면, 서비스는 카카오 인증 서버로 인가 코드 발급을 요청한다. */
    public String kakaoLogin(String code) throws JsonProcessingException {

        //1. 인가 코드로 액세스 토큰 요청 -> 아래 getToken 으로 넘어감
        String accessToken = getToken(code);

        //2. 토큰으로 카카오 api호출 : "액세스토큰"으로 "카카오 사용자 정보"가저오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        return null;
    }

    public String getToken(String code) throws JsonProcessingException {
        // 인가 코드 요청 URL 만들기
        /*이 부분은 kakao, google 등 다른 소셜 로그인 서비스마다 달라질듯.*/
        URI uri = UriComponentsBuilder
                .fromUriString("http://kauth.kakao.com")
                .path("oauth/token")
                .encode()
                .build()
                .toUri();


        //HTTP header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencode;charset=utf-8");

        //HTTP Body 생성
        /*Json 형태로 요청할 body 를 만드는 것인가?*/
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "3f5211af4262100b1a6a78532a532b01");
        // ↑ 3f5211af4262100b1a6a78532a532b01 입력 (Needle application id)

        body.add("redirect_uri", "http://localhost:8080/kp3c/user/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity, String.class);

        // HTTP 응답 (Json) -> 액세스 토큰 파싱
        /*액세스 토큰이 있어야 사용자 인증, 카카오 API를 사용할수 있음*/
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    /*받은 액세스 토큰으로 카카오에 등록된 유저 정보 받아오기*/
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        //요청 URL 만들기
        /*이건 카카오에서 제공하는 주소겠지?*/
        URI uri = UriComponentsBuilder
                .fromUriString("http://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        //HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken); //어디서 많이 본 authorization
        headers.add("Content-type", "application/x-www-form-urlencode;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_acoount")
                .get("email").asText();

        log.info("카카오 사용자 정보 " + id + ", " + nickname + " , " + email);
        return new KakaoUserInfoDto(id, nickname, email);

        /*요청을 통해 카카오에서 정보를 받아오게 되었다!*/


    }
}