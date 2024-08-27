package com.nhnacademy.batch.batch.book.tag.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nhnacademy.batch.batch.book.tag.repository.TagCustomRepository;
import com.nhnacademy.batch.batch.book.tag.response.BookTagResponse;
import com.nhnacademy.batch.entity.booktag.QBookTag;
import com.nhnacademy.batch.entity.tag.QTag;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class TagCustomRepositoryImpl implements TagCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private static final QTag qTag = QTag.tag;
	private static final QBookTag qBookTag = QBookTag.bookTag;

	public TagCustomRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<BookTagResponse> bookCategories(long minBookId, long maxBookId) {
		return jpaQueryFactory.select(
				Projections.constructor(BookTagResponse.class,
					qBookTag.book.id,
					qTag.name))
			.from(qBookTag)
			.join(qBookTag.tag, qTag)
			.where(qBookTag.book.id.gt(minBookId)
				.and(qBookTag.book.id.lt(maxBookId)))
			.fetch();
	}
}
