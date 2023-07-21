package com.example.kp3coutsourcingproject.comment.repository;

import com.example.kp3coutsourcingproject.comment.entity.Comment;
import com.example.kp3coutsourcingproject.comment.entity.QComment;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.kp3coutsourcingproject.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository{
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Comment> findAllByPost(Post post) {
		return queryFactory.selectFrom(comment)
				.leftJoin(comment.parent)
				.fetchJoin()
				.where(comment.post.id.eq(post.getId()))
				.orderBy(comment.parent.id.asc().nullsFirst(),comment.createdAt.asc())
				.fetch();
	}

}
