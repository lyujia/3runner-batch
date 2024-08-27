package com.nhnacademy.batch.batch.book.category.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nhnacademy.batch.batch.book.category.repository.CategoryCustomRepository;
import com.nhnacademy.batch.batch.book.category.response.BookCategoryResponse;
import com.nhnacademy.batch.entity.bookcategory.QBookCategory;
import com.nhnacademy.batch.entity.category.QCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private static final QCategory qCategory = QCategory.category;
	private static final QBookCategory qBookCategory = QBookCategory.bookCategory;

	public CategoryCustomRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<BookCategoryResponse> bookCategories(long minBookId, long maxBookId) {
		return jpaQueryFactory.select(
				Projections.constructor(BookCategoryResponse.class,
					qBookCategory.book.id,
					qCategory.name))
			.from(qBookCategory)
			.join(qBookCategory.category, qCategory)
			.where(qBookCategory.book.id.gt(minBookId)
				.and(qBookCategory.book.id.lt(maxBookId)))
			.fetch();
	}

}
