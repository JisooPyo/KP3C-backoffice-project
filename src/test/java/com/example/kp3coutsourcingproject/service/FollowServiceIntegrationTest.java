package com.example.kp3coutsourcingproject.service;

import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.FollowRepository;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import com.example.kp3coutsourcingproject.user.service.FollowService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 포트를 랜덤으로 설정
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowServiceIntegrationTest {
    @Autowired
    FollowService followService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    User userA, userB, userC;

    private void userSetup() {
        userA = new User("userA", "userA", "Abc012#!", "hi", "userA@email.com", UserRoleEnum.USER);
        userB = new User("userB", "userB", "Abc012#!", "hi", "userB@email.com", UserRoleEnum.USER);
        userC = new User("userC", "userC", "Abc012#!", "hi", "userC@email.com", UserRoleEnum.USER);
        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);
    }

    @Test
    @Order(1)
    @DisplayName("팔로우 이후 팔로워 팔로잉 조회") // A -> B, B -> C, A -> C
    void test1() {
        //given
        this.userSetup();

        //when
        followService.follow("userB", userA);
        followService.follow("userC", userB);
        followService.follow("userC", userA);

        //then
        List<ProfileDto> userCFollowers = followService.getFollowers("userC");
        List<ProfileDto> userAFollowing = followService.getFollowing("userA");
        List<ProfileDto> userCFollowing = followService.getFollowing("userC");

        assertThat(userCFollowers)
                .extracting("username")
                .containsOnly("userA", "userB");

        assertThat(userAFollowing)
                .extracting("username")
                .containsOnly("userB", "userC");

        assertTrue(userCFollowing.isEmpty());
        assertTrue(followService.isFollowing("userB", userA));
        assertTrue(followService.isFollowing("userC", userB));
        assertTrue(followService.isFollowing("userC", userA));
        assertFalse(followService.isFollowing("userA", userB));
    }

    @Test
    @Order(2)
    @DisplayName("언팔로우 이후 팔로워 팔로잉 조회") // A -> B, A -> C
    void test2() {
        //when
        followService.unfollow("userC", userB);

        //then
        List<ProfileDto> userCFollowers = followService.getFollowers("userC");
        List<ProfileDto> userAFollowing = followService.getFollowing("userA");
        List<ProfileDto> userBFollowing = followService.getFollowing("userB");

        assertThat(userCFollowers)
                .extracting("username")
                .containsOnly("userA");

        assertThat(userAFollowing)
                .extracting("username")
                .containsOnly("userB", "userC");

        assertTrue(userBFollowing.isEmpty());
        assertTrue(followService.isFollowing("userC", userA));
        assertFalse(followService.isFollowing("userC", userB));
    }

    @Test
    @Order(3)
    @DisplayName("자기 자신을 팔로우 또는 언팔로우 시도하는 경우")
    void test3() {
        IllegalArgumentException followException = assertThrows(IllegalArgumentException.class, () ->
                followService.follow("userA", userA));
        assertEquals("자기 자신을 팔로우할 수 없습니다.", followException.getMessage());

        IllegalArgumentException unfollowException = assertThrows(IllegalArgumentException.class, () ->
                followService.unfollow("userB", userB));
        assertEquals("자기 자신을 언팔로우할 수 없습니다.", unfollowException.getMessage());
    }
}
