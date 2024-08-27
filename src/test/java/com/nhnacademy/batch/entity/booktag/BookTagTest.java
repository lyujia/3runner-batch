package com.nhnacademy.batch.entity.booktag;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.tag.Tag;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookTagTest {

	@Autowired
	private EntityManager entityManager;

	private Book book;
	private Tag tag;

	@BeforeEach
	void setUp() {
		book = new Book();
		book.setTitle("Test Book");
		book.setDescription("Test Description");
		book.setPrice(1000);
		book.setQuantity(10);
		book.setSellingPrice(1200);
		book.setViewCount(0);
		book.setPacking(true);
		book.setAuthor("Test Author");
		book.setIsbn("123-456-789");
		book.setPublisher("Test Publisher");

		tag = new Tag();
		tag.setName("Test Tag");

		entityManager.persist(book);
		entityManager.persist(tag);
	}

	@Test
	void testBookTagEntity() {
		// given
		BookTag bookTag = new BookTag(book, tag);
		bookTag.setBook(book);
		bookTag.setTag(tag);

		// when
		entityManager.persist(bookTag);
		entityManager.flush();
		entityManager.clear();

		// then
		BookTag foundBookTag = entityManager.find(BookTag.class, bookTag.getId());
		assertThat(foundBookTag).isNotNull();
		assertThat(foundBookTag.getId()).isEqualTo(bookTag.getId());
		assertThat(foundBookTag.getBook().getId()).isEqualTo(bookTag.getBook().getId());
		assertThat(foundBookTag.getTag().getId()).isEqualTo(bookTag.getTag().getId());
		assertThat(foundBookTag.getTag().getName()).isEqualTo(tag.getName());
	}
}
