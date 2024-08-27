package com.nhnacademy.batch.book.category.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nhnacademy.batch.batch.book.category.repository.impl.CategoryCustomRepositoryImpl;
import com.nhnacademy.batch.batch.book.category.response.BookCategoryResponse;
import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.bookcategory.BookCategory;
import com.nhnacademy.batch.entity.category.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CategoryCustomRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	private CategoryCustomRepositoryImpl categoryCustomRepository;

	@TestConfiguration
	static class TestConfig {
		@Bean
		public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
			return new JPAQueryFactory(entityManager);
		}
	}

	@BeforeEach
	void setUp() {
		categoryCustomRepository = new CategoryCustomRepositoryImpl(entityManager);
		setupTestData();
	}

	private void setupTestData() {
		Category category1 = new Category("Category1");
		Category category2 = new Category("Category2");

		entityManager.persist(category1);
		entityManager.persist(category2);

		Book book1 = new Book();
		book1.setTitle("Book 1");
		book1.setPrice(100);
		book1.setSellingPrice(90);
		book1.setAuthor("Author 1");
		book1.setPublisher("Publisher 1");
		book1.setIsbn("1234567890123");

		Book book2 = new Book();
		book2.setTitle("Book 2");
		book2.setPrice(100);
		book2.setSellingPrice(90);
		book2.setAuthor("Author 2");
		book2.setPublisher("Publisher 2");
		book2.setIsbn("1234567890124");

		Book book3 = new Book();

		book3.setTitle("Book 3");
		book3.setPrice(100);
		book3.setSellingPrice(90);
		book3.setAuthor("Author 3");
		book3.setPublisher("Publisher 3");
		book3.setIsbn("123467890125");

		entityManager.persist(book1);
		entityManager.persist(book2);
		entityManager.persist(book3);

		BookCategory bookCategory1 = new BookCategory();
		bookCategory1.setBook(book1);
		bookCategory1.setCategory(category1);

		BookCategory bookCategory2 = new BookCategory();
		bookCategory2.setBook(book2);
		bookCategory2.setCategory(category2);

		entityManager.persist(bookCategory1);
		entityManager.persist(bookCategory2);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testBookCategories() {
		long minBookId = 0L;
		long maxBookId = 2L;

		List<BookCategoryResponse> results = categoryCustomRepository.bookCategories(minBookId, maxBookId);

		assertThat(results).hasSize(1);
		assertThat(results.get(0).getBookId()).isEqualTo(1L);
		assertThat(results.get(0).getName()).isEqualTo("Category1");
	}
}
