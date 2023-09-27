# Needle은 SNS하지 마라~

스레드와 트위터를 참고하여 SNS 사이트를 만들어보고 백오피스까지 구현해보는 프로젝트

> 💭 [KPT 회고](https://argente29.tistory.com/116)

### 프로젝트 요구 사항
<details>
<summary>필수 기능</summary>

#### 1. 사용자 인증 기능

* 회원가입
  - username, password는 정해진 형식이 있다.
  - 회원 권한 부여 - ADMIN, USER

* 로그인 및 로그아웃

#### 2. 프로필 관리

* 프로필 수정
  - 이름, 한 줄 소개같은 기본적인 정보를 볼 수 있어야 하며 수정할 수 있어야 한다.
  - 비밀번호 수정이 가능해야 함
  - 최근 3번 안에 사용한 비밀번호는 사용할 수 없도록 제한한다.

#### 3. 게시물 CRUD

* 전체 게시물 조회
* 인가 개념 적용

#### 4. 댓글 CRUD

* 전체 댓글 조회
* 인가 개념 적용

</details>

<details>
<summary>추가 기능 1</summary>

#### 1. 소셜 로그인 기능 - 네이버, 카카오

#### 2. 백오피스
  * 회원 관리
    * 회원 조회
    * 회원 정보 수정(권한 수정)
    * 회원 삭제
    * 회원 차단
  * 게시글, 댓글 관리
    *  공지글 등록
    *  모든 게시글 수정
    *  모든 게시글 삭제

#### 3. 프론트엔드

#### 4. 좋아요
  * 게시물 및 댓글 좋아요/좋아요 취소
  * 본인이 작성한 게시물과 댓글에 좋아요 남길 수 없도록 하기

#### 5. 팔로우
  * 특정 사용자를 팔로우/언팔로우
  * 팔로우하는 사용자의 게시물을 볼 수 있도록 하기
    
</details>

<details>
<summary>추가 기능 2</summary>

#### 1. 사진 업로드
* 사진 저장할 때는 반드시 AWS S3 이용

#### 2. 게시물 멀티미디어 지원 기능 구현
* 게시물 본문에 사진, 영상 미디어 포함 가능
* 게시물 수정시 첨부된 미디어 수정 가능
* 미디어 첨부 시 AWS S3 사용

#### 3. AWS를 이용하여 서비스 배포
* AWS EC2를 이용해서 배포
  * EC2 역할 이해
  * ubuntu 명령어들을 이해
  * 웹서버, 웹어플리케이션 서버의 차이를 이해
  * Nginx, gunicorn의 역할과 설정파일을 이해

#### 4. HTTP를 HTTPS로 업그레이드 하기
</details>

<br>

## 기술 스택

<img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=OpenJDK&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/spring-6DB33F?style=flat-square&logo=spring&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/gradle-02303A?style=flat-square&logo=gradle&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Query DSL-0769AD?style=flat-square&logo=&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white">
<br>
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=html5&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/CSS-1572B6?style=flat-square&logo=css3&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black">
<br>
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white">&nbsp;
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=postman&logoColor=white">

<br>

## 프로젝트 명세

### ERD
<image src="https://github.com/JisooPyo/KP3C-backoffice-project/assets/130378232/488fab1b-7789-4f0b-b805-f6b6b270b4d5" width=800px>

### API 명세

[API 명세](https://gilded-meeting-f87.notion.site/9-KP3C-Needle-SNS-a61462b6239444d1b8aaf13f5e8e7258)

### WireFrame

<image src="https://github.com/JisooPyo/KP3C-backoffice-project/assets/130378232/86adf0c2-a1c2-43ad-a2ab-e27d1144d377">


---

## 우리 프로젝트에서 구현한 기능

### 회원가입, 로그인, 로그아웃

* username, email 둘 다 로그인 할 수 있게끔 함.
* 로직 - String usernameOrEmail이 들어오면 먼저 username에서 찾아보고 다음 email에서 찾아본 후 일치하면 로그인 성공
* 로그인 성공 시 accessToken, refreshToken 발급
* 로그아웃 기능 구현 - Redis blacklist 이용

### 팔로우

* 유저는 다른 유저를 팔로우, 언팔로우할 수 있다.
* 유저는 자신의 팔로워, 팔로잉 목록을 조회할 수 있다.

### 글 관련 기능

* 글에 답글을 남길 수 있다.
* 홈피드 조회 - 내가 팔로우 한 사람 + 내가 쓴 글 / 마이피드 조회 - 내가 쓴 글 - MySQL 이용한 메서드 & Redis 이용한 메서드
* 모든 글 계층형으로 조회 가능

### 백오피스

* 관리자만 관리자 페이지 접근 가능
* 전체 회원 / 회원 한 명 조회
* 회원 정보 수정 - 회원 프로필 수정, 회원 비밀번호 변경, 회원 권한 변경
* 회원 삭제(강제 탈퇴) - 관리자는 삭제 불가능
* 공지사항 CRUD
* 유저가 쓴 글 RUD 가능
* 페이징 기능
* 게시글 조회 - 생성 순서를 기준으로
* 회원 조회 - 가입 순서를 기준으로, 팔로워 수 / 팔로잉 수를 기준으로
* 회원 차단 / 계정 잠금 기능

### 멀티미디어

* 유저는 자신의 프로필을 수정할 수 있다. (프로필 이미지도 수정 가능)
* 유저는 회원가입 시, 자신의 프로필 사진을 설정할 수 있다.
* 프로필, 회원가입 부분 프론트엔드 구현
* 유저는 글에 멀티미디어를 올릴 수 있다.(AWS S3 이용)

### 카카오 로그인

* 카카오 로그인 구현 - 로그인 성공시 accessToken, refreshToken 발급

## Contact

👩‍💻 김은비 - [Blog](https://velog.io/@eunb1) / [Github](https://github.com/eunb1)<br>
👩‍💻 최정은 - [Blog](https://velog.io/@temprmn) / [Github](https://github.com/jungeun5-choi)<br>
👨‍💻 최종우 - [Blog](https://jonggae.tistory.com/) / [Github](https://github.com/Jonggae)<br>
👩‍💻 최혜원 - [Blog](https://velog.io/@hyewon0218) / [Github](https://github.com/hyewon218)<br>
👩‍💻 표지수 - [Blog](https://argente29.tistory.com/) / [Github](https://github.com/JisooPyo)<br>
