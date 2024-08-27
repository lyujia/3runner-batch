package com.nhnacademy.batch.entity.book;

import static org.assertj.core.api.Assertions.*;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nhnacademy.batch.entity.bookcategory.BookCategory;
import com.nhnacademy.batch.entity.bookimage.BookImage;
import com.nhnacademy.batch.entity.bookimage.enums.BookImageType;
import com.nhnacademy.batch.entity.booktag.BookTag;
import com.nhnacademy.batch.entity.totalimage.TotalImage;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookTest {

	@Autowired
	private EntityManager entityManager;

	private Book book;

	@BeforeEach
	void setUp() {
		book = new Book();
		book.setTitle("Test Book");
		book.setDescription("Test Description");
		book.setPublishedDate(ZonedDateTime.now());
		book.setPrice(1000);
		book.setQuantity(10);
		book.setSellingPrice(1200);
		book.setViewCount(0);
		book.setPacking(true);
		book.setAuthor("Test Author");
		book.setIsbn("123-456-789");
		book.setPublisher("Test Publisher");
	}

	@Test
	void testBookEntityCreation() {
		// given
		// already set up

		// when
		entityManager.persist(book);
		entityManager.flush();
		entityManager.clear();

		// then
		Book foundBook = entityManager.find(Book.class, book.getId());
		assertThat(foundBook).isNotNull();
		assertThat(foundBook.getTitle()).isEqualTo("Test Book");
		assertThat(foundBook.getDescription()).isEqualTo("Test Description");
		assertThat(foundBook.getPublishedDate()).isNotNull();
		assertThat(foundBook.getPrice()).isEqualTo(1000);
		assertThat(foundBook.getQuantity()).isEqualTo(10);
		assertThat(foundBook.getSellingPrice()).isEqualTo(1200);
		assertThat(foundBook.getViewCount()).isEqualTo(0);
		assertThat(foundBook.isPacking()).isTrue();
		assertThat(foundBook.getAuthor()).isEqualTo("Test Author");
		assertThat(foundBook.getIsbn()).isEqualTo("123-456-789");
		assertThat(foundBook.getPublisher()).isEqualTo("Test Publisher");
		assertThat(foundBook.getCreatedAt()).isNotNull();
	}

	@Test
	void testBookEntityWithRelationships() {
		// given
		BookCategory bookCategory = new BookCategory();
		bookCategory.setBook(book);

		BookTag bookTag = new BookTag();
		bookTag.setBook(book);

		TotalImage totalImage = new TotalImage("test.url");
		entityManager.persist(totalImage);

		BookImage bookImage = new BookImage(BookImageType.MAIN, book, totalImage);

		book.getBookCategoryList().add(bookCategory);
		book.getBookTagList().add(bookTag);
		book.getBookImageList().add(bookImage);

		// when
		entityManager.persist(book);
		entityManager.flush();
		entityManager.clear();

		// then
		Book foundBook = entityManager.find(Book.class, book.getId());
		assertThat(foundBook).isNotNull();
		assertThat(foundBook.getBookCategoryList()).hasSize(1);
		assertThat(foundBook.getBookTagList()).hasSize(1);
		assertThat(foundBook.getBookImageList()).hasSize(1);
	}
}
