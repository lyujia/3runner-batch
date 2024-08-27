package com.nhnacademy.batch.entity.bookimage;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.bookimage.enums.BookImageType;
import com.nhnacademy.batch.entity.totalimage.TotalImage;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookImageTest {

	@Autowired
	private EntityManager entityManager;

	@Test
	void testBookImageEntity() {
		// given
		Book book1 = new Book();
		book1.setTitle("Book 1");
		book1.setPrice(100);
		book1.setSellingPrice(90);
		book1.setAuthor("Author 1");
		book1.setPublisher("Publisher 1");
		book1.setIsbn("1234567890123");
		entityManager.persist(book1);

		TotalImage totalImage = new TotalImage("test.com");
		entityManager.persist(totalImage);

		BookImage bookImage = new BookImage(BookImageType.MAIN, book1, totalImage);
		bookImage.setTotalImage(totalImage);
		bookImage.setBook(book1);

		// when
		entityManager.persist(bookImage);
		entityManager.flush();
		entityManager.clear();

		// then
		BookImage foundBookImage = entityManager.find(BookImage.class, bookImage.getId());
		assertThat(foundBookImage).isNotNull();
		assertThat(foundBookImage.getType()).isEqualTo(BookImageType.MAIN);
		assertThat(foundBookImage.getBook().getId()).isEqualTo(book1.getId());
		assertThat(foundBookImage.getBook().getTitle()).isEqualTo(book1.getTitle());
		assertThat(foundBookImage.getBook().getPrice()).isEqualTo(book1.getPrice());
		assertThat(foundBookImage.getBook().getDescription()).isEqualTo(book1.getDescription());
		assertThat(foundBookImage.getBook().getQuantity()).isEqualTo(book1.getQuantity());
		assertThat(foundBookImage.getTotalImage().getUrl()).isEqualTo(totalImage.getUrl());
	}
}
