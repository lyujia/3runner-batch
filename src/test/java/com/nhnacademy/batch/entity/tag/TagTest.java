package com.nhnacademy.batch.entity.tag;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.booktag.BookTag;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TagTest {

	@Autowired
	private EntityManager entityManager;

	private Book book;

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

		entityManager.persist(book);
	}

	@Test
	void testTagEntity() {
		// given
		Tag tag = new Tag("Test Tag");

		// when
		entityManager.persist(tag);
		entityManager.flush();
		entityManager.clear();

		// then
		Tag foundTag = entityManager.find(Tag.class, tag.getId());
		assertThat(foundTag).isNotNull();
		assertThat(foundTag.getName()).isEqualTo("Test Tag");
	}

	@Test
	void testTagEntityWithRelationships() {
		// given
		Tag tag = new Tag("Test Tag");
		BookTag bookTag = new BookTag(book, tag);
		tag.getBookTagList().add(bookTag);

		// when
		entityManager.persist(tag);
		entityManager.flush();
		entityManager.clear();

		// then
		Tag foundTag = entityManager.find(Tag.class, tag.getId());
		assertThat(foundTag).isNotNull();
		assertThat(foundTag.getBookTagList()).hasSize(1);
		assertThat(foundTag.getBookTagList().get(0).getBook().getId()).isEqualTo(book.getId());
		assertThat(foundTag.getBookTagList().get(0).getTag().getName()).isEqualTo(tag.getName());
	}
}
