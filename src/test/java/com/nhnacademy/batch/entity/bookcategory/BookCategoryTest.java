package com.nhnacademy.batch.entity.bookcategory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.category.Category;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookCategoryTest {

	@Autowired
	private EntityManager entityManager;

	private Book book;
	private Category category;

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

		category = new Category();
		category.setName("Test Category");

		entityManager.persist(book);
		entityManager.persist(category);
	}

	@Test
	void testBookCategoryEntity() {
		// given
		BookCategory bookCategory = new BookCategory();
		bookCategory.setBook(book);
		bookCategory.setCategory(category);

		// when
		entityManager.persist(bookCategory);
		entityManager.flush();
		entityManager.clear();

		// then
		BookCategory foundBookCategory = entityManager.find(BookCategory.class, bookCategory.getId());
		assertThat(foundBookCategory).isNotNull();
		assertThat(foundBookCategory.getBook().getId()).isEqualTo(book.getId());
		assertThat(foundBookCategory.getCategory().getId()).isEqualTo(category.getId());
	}
}
