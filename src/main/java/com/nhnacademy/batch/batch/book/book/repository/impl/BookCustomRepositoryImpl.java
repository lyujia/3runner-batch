package com.nhnacademy.batch.batch.book.book.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nhnacademy.batch.batch.book.book.repository.BookCustomRepository;
import com.nhnacademy.batch.batch.book.book.response.BookDocument;
import com.nhnacademy.batch.entity.book.QBook;
import com.nhnacademy.batch.entity.bookimage.QBookImage;
import com.nhnacademy.batch.entity.bookimage.enums.BookImageType;
import com.nhnacademy.batch.entity.totalimage.QTotalImage;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class BookCustomRepositoryImpl implements BookCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private static final QBook qBook = QBook.book;
	private static final QBookImage qBookImage = QBookImage.bookImage;
	private static final QTotalImage qTotalImage = QTotalImage.totalImage;

	public BookCustomRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<BookDocument> bookDocumentList(long minBookId, long maxBookId) {
		return jpaQueryFactory.select(
				Projections.constructor(BookDocument.class,
					qBook.id,
					qBook.title,
					qBook.price,
					qBook.sellingPrice,
					qBook.author,
					qTotalImage.url,
					qBook.publisher))
			.distinct()
			.from(qBook)
			.leftJoin(qBookImage)
			.on(qBookImage.book.id.eq(qBook.id).and(qBookImage.type.eq(BookImageType.MAIN)))
			.leftJoin(qTotalImage)
			.on(qTotalImage.bookImage.id.eq(qBookImage.id))
			.groupBy(qBook.id, qBook.title, qBook.price, qBook.sellingPrice, qBook.author, qTotalImage.url)
			.where(qBook.id.gt(minBookId)
				.and(qBook.id.lt(maxBookId)))
			.fetch();
	}
}
