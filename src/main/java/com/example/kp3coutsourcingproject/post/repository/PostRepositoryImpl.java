package com.example.kp3coutsourcingproject.post.repository;

import com.example.kp3coutsourcingproject.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.kp3coutsourcingproject.post.entity.QPost.post;
import static com.example.kp3coutsourcingproject.user.entity.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{
	private final JPAQueryFactory queryFactory;

	// 홈피드 가져오기
	@Override
	public List<Post> getHomeFeed(Long userId) {
		// 팔로우하는 사람들의 게시물을 가져오는 서브쿼리
		List<Post> followeePosts = queryFactory
				.select(post)
				.from(post)
				.innerJoin(follow)
				.on(post.user.id.eq(follow.followee.id))
				.where(follow.follower.id.eq(userId))
				.fetch();

		// 사용자 본인의 게시물을 가져오는 메인 쿼리
		List<Post> userPosts = queryFactory
				.select(post)
				.from(post)
				.where(post.user.id.eq(userId))
				.fetch();

		// 두 리스트를 합치고, id를 기준으로 내림차순으로 정렬하여 최종 홈 피드를 얻기.
		followeePosts.addAll(userPosts);
		followeePosts.sort((p1, p2) -> p2.getId().compareTo(p1.getId()));

		return followeePosts;

	}

	// 유저가 쓴 글 가져오기
	@Override
	public List<Post> getUserFeed(Long userId) {
		// 사용자 본인의 게시물을 가져오는 쿼리
		List<Post> userPosts = queryFactory
				.select(post)
				.from(post)
				.where(post.user.id.eq(userId))
				.fetch();

		userPosts.sort((p1, p2) -> p2.getId().compareTo(p1.getId()));
		return userPosts;
	}
}
