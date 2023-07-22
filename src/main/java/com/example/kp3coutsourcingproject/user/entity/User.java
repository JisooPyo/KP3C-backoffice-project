package com.example.kp3coutsourcingproject.user.entity;

import com.example.kp3coutsourcingproject.user.dto.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.transaction.annotation.Transactional;

import static com.example.kp3coutsourcingproject.common.util.AccessUtils.getSafe;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String introduction;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(columnDefinition = "char(1)")
    private Boolean enabled; // 액세스 가능

    private Long kakaoId;

    @ColumnDefault("0")
    @Column(name = "follower_count")
    private Integer followerCount; // 팔로워 수
    @ColumnDefault("0")
    @Column(name = "following_count")
    private Integer followingCount; // 팔로잉 수

    @Column(nullable = false)
    private String imageFile;

    public User(String username, String nickname, String password, String introduction, String email, UserRoleEnum role, String image) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.introduction = introduction;
        this.email = email;
        this.role = role;
        this.enabled = getSafe(this.enabled, true);
        this.imageFile = image;
    }

    public User(String nickname, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public void update(ProfileRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.nickname = requestDto.getNickname();
        this.introduction = requestDto.getIntroduction();
        this.imageFile = requestDto.getImageUrl();
    }

    public User updateKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }



    @Transactional
    public void updateFollowerCount(Integer value) {
        this.followerCount += value;
    }
    @Transactional
    public void updateFollowingCount(Integer value) {
        this.followingCount += value;
    }
}
