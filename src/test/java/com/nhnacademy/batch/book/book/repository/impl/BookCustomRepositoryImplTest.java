package com.nhnacademy.batch.book.book.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nhnacademy.batch.batch.book.book.repository.BookCustomRepository;
import com.nhnacademy.batch.batch.book.book.repository.impl.BookCustomRepositoryImpl;
import com.nhnacademy.batch.batch.book.book.response.BookDocument;
import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.bookimage.BookImage;
import com.nhnacademy.batch.entity.bookimage.enums.BookImageType;
import com.nhnacademy.batch.entity.totalimage.TotalImage;

import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookCustomRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	private BookCustomRepository bookCustomRepository;

	Long bookId;

	@BeforeEach
	public void setUp() {
		bookCustomRepository = new BookCustomRepositoryImpl(entityManager);

		// Sample data setup
		Book book1 = new Book();
		book1.setTitle("Book 1");
		book1.setPrice(100);
		book1.setSellingPrice(90);
		book1.setAuthor("Author 1");
		book1.setPublisher("Publisher 1");
		book1.setIsbn("1234567890123");
		entityManager.persist(book1);
		bookId = book1.getId();
		entityManager.flush();

		TotalImage totalImage1 = new TotalImage("http://example.com/book1.jpg");
		BookImage bookImage1 = new BookImage(BookImageType.MAIN, null, null);
		bookImage1.setBook(book1);
		bookImage1.setTotalImage(totalImage1);
		entityManager.persist(bookImage1);

		entityManager.persist(totalImage1);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	public void testBookDocumentList() {
		List<BookDocument> bookDocuments = bookCustomRepository.bookDocumentList(0, 100);

		assertThat(bookDocuments).hasSize(1);
		BookDocument bookDocument = bookDocuments.getFirst();
		assertThat(bookDocument.getId()).isEqualTo(bookId);
		assertThat(bookDocument.getTitle()).isEqualTo("Book 1");
		assertThat(bookDocument.getPrice()).isEqualTo(100);
		assertThat(bookDocument.getSellingPrice()).isEqualTo(90);
		assertThat(bookDocument.getAuthor()).isEqualTo("Author 1");
		assertThat(bookDocument.getPublisher()).isEqualTo("Publisher 1");
		assertThat(bookDocument.getThumbnail()).isEqualTo("http://example.com/book1.jpg");
	}
}
