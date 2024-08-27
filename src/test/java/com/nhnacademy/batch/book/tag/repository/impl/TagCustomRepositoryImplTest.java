package com.nhnacademy.batch.book.tag.repository.impl;

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

import com.nhnacademy.batch.batch.book.tag.repository.impl.TagCustomRepositoryImpl;
import com.nhnacademy.batch.batch.book.tag.response.BookTagResponse;
import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.booktag.BookTag;
import com.nhnacademy.batch.entity.tag.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TagCustomRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	private TagCustomRepositoryImpl tagCustomRepository;

	@TestConfiguration
	static class TestConfig {
		@Bean
		public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
			return new JPAQueryFactory(entityManager);
		}
	}

	@BeforeEach
	void setUp() {
		tagCustomRepository = new TagCustomRepositoryImpl(entityManager);
		setupTestData();
	}

	private void setupTestData() {
		Tag tag1 = new Tag("Tag1");
		Tag tag2 = new Tag();
		tag2.setName("Tag2");

		entityManager.persist(tag1);
		entityManager.persist(tag2);

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

		BookTag bookTag1 = new BookTag(book1, tag1);

		BookTag bookTag2 = new BookTag();
		bookTag2.setBook(book2);
		bookTag2.setTag(tag2);

		entityManager.persist(bookTag1);
		entityManager.persist(bookTag2);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testBookCategories() {
		long minBookId = 0L;
		long maxBookId = 2L;

		List<BookTagResponse> results = tagCustomRepository.bookCategories(minBookId, maxBookId);

		assertThat(results).hasSize(1);
		assertThat(results.get(0).getBookId()).isEqualTo(1L);
		assertThat(results.get(0).getName()).isEqualTo("Tag1");
	}
}
