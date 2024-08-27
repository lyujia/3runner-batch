package com.nhnacademy.batch.book.book.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.batch.batch.book.book.repository.BookDocumentRepository;
import com.nhnacademy.batch.batch.book.book.repository.BookRedisRepository;
import com.nhnacademy.batch.batch.book.book.repository.BookRepository;
import com.nhnacademy.batch.batch.book.book.repository.BookRestTemplate;
import com.nhnacademy.batch.batch.book.book.repository.JsonFileLoadRepository;
import com.nhnacademy.batch.batch.book.book.response.BookDocument;
import com.nhnacademy.batch.batch.book.book.service.impl.BookServiceImpl;
import com.nhnacademy.batch.batch.book.category.repository.CategoryCustomRepository;
import com.nhnacademy.batch.batch.book.category.response.BookCategoryResponse;
import com.nhnacademy.batch.batch.book.tag.repository.TagCustomRepository;
import com.nhnacademy.batch.batch.book.tag.response.BookTagResponse;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

class BookServiceImplTest {
	@Mock
	private BookRepository bookRepository;

	@Mock
	private JsonFileLoadRepository jsonFileLoadRepository;

	@Mock
	private BookRestTemplate bookRestTemplate;

	@Mock
	private BookDocumentRepository bookDocumentRepository;

	@Mock
	private BookRedisRepository bookRedisRepository;

	@Mock
	private TagCustomRepository tagCustomRepository;

	@Mock
	private CategoryCustomRepository categoryCustomRepository;

	@InjectMocks
	private BookServiceImpl bookService;

	private ListAppender<ILoggingEvent> listAppender;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(
			BookServiceImpl.class);
		listAppender = new ListAppender<>();
		listAppender.start();
		logger.addAppender(listAppender);
	}

	@Test
	public void testAllBookToElasticSearchFromDB() {
		LocalDate currentDate = LocalDate.now();
		String today = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String indexName = "3runner_book_" + today;

		BookDocument book = new BookDocument();
		book.setId(1L);
		book.setTitle("Book Title");
		book.setAuthor("Author");
		book.setThumbnail("Thumbnail");
		book.setPublisher("Publisher");
		book.setPrice(1000);
		book.setSellingPrice(1000);
		book.setTagList(new ArrayList<>());
		book.setCategoryList(new ArrayList<>());

		BookTagResponse bookTagResponse1 = new BookTagResponse(1L, "tag1");

		BookTagResponse bookTagResponse2 = new BookTagResponse();
		bookTagResponse2.setBookId(1L);
		bookTagResponse2.setName("tag2");
		BookTagResponse bookTagResponse3 = new BookTagResponse();
		bookTagResponse3.setBookId(1L);
		bookTagResponse3.setName("tag3");

		BookCategoryResponse bookCategoryResponse1 = new BookCategoryResponse();
		bookCategoryResponse1.setBookId(1L);
		bookCategoryResponse1.setName("category1");
		BookCategoryResponse bookCategoryResponse2 = new BookCategoryResponse();
		bookCategoryResponse2.setBookId(1L);
		bookCategoryResponse2.setName("category2");
		BookCategoryResponse bookCategoryResponse3 = new BookCategoryResponse();
		bookCategoryResponse3.setBookId(1L);
		bookCategoryResponse3.setName("category3");

		when(jsonFileLoadRepository.jsonFileLoad(anyString())).thenReturn("index settings");
		when(bookRepository.maxBookId()).thenReturn(1000L);
		when(bookRepository.bookDocumentList(anyLong(), anyLong())).thenReturn(List.of(book));
		when(tagCustomRepository.bookCategories(anyLong(), anyLong())).thenReturn(
			List.of(bookTagResponse1, bookTagResponse2, bookTagResponse3));
		when(categoryCustomRepository.bookCategories(anyLong(), anyLong())).thenReturn(List.of(bookCategoryResponse1,
			bookCategoryResponse2, bookCategoryResponse3));
		when(bookDocumentRepository.makeBulkBody(anyList(), anyString())).thenReturn("bulk body");
		when(bookDocumentRepository.changeAlias(anyString(), anyString())).thenReturn("aliasBody");

		bookService.allBookToElasticSearchFromDB();

		verify(bookRestTemplate, times(1)).saveNewIndex(anyString(), anyString());
		verify(bookRestTemplate, times(4)).sendBulk(anyString(), anyString());
		verify(bookRestTemplate, times(1)).sendAlias(anyString());
	}

	@Test
	public void testAllBookToElasticSearchFromDBException() {
		BookDocument book = new BookDocument();
		book.setId(1L);
		book.setTitle("Book Title");
		book.setAuthor("Author");
		book.setThumbnail("Thumbnail");
		book.setPublisher("Publisher");
		book.setPrice(1000);
		book.setSellingPrice(1000);
		book.setTagList(List.of());
		book.setCategoryList(new ArrayList<>());
		BookTagResponse bookTagResponse1 = new BookTagResponse();
		bookTagResponse1.setBookId(1L);
		bookTagResponse1.setName("tag1");

		BookTagResponse bookTagResponse2 = new BookTagResponse();
		bookTagResponse2.setBookId(1L);
		bookTagResponse2.setName("tag2");
		BookTagResponse bookTagResponse3 = new BookTagResponse();
		bookTagResponse3.setBookId(1L);
		bookTagResponse3.setName("tag3");

		BookCategoryResponse bookCategoryResponse1 = new BookCategoryResponse();
		bookCategoryResponse1.setBookId(1L);
		bookCategoryResponse1.setName("category1");
		BookCategoryResponse bookCategoryResponse2 = new BookCategoryResponse();
		bookCategoryResponse2.setBookId(1L);
		bookCategoryResponse2.setName("category2");
		BookCategoryResponse bookCategoryResponse3 = new BookCategoryResponse();
		bookCategoryResponse3.setBookId(1L);
		bookCategoryResponse3.setName("category3");

		when(jsonFileLoadRepository.jsonFileLoad(anyString())).thenReturn("index settings");
		when(bookRepository.maxBookId()).thenReturn(1000L);

		when(bookRestTemplate.sendBulk(any(), any())).thenThrow(new RuntimeException());
		bookService.allBookToElasticSearchFromDB();
		when(bookRepository.bookDocumentList(anyLong(), anyLong())).thenReturn(List.of(book));
		when(tagCustomRepository.bookCategories(anyLong(), anyLong())).thenReturn(
			List.of(bookTagResponse1, bookTagResponse2, bookTagResponse3));
		when(categoryCustomRepository.bookCategories(anyLong(), anyLong())).thenReturn(List.of(bookCategoryResponse1,
			bookCategoryResponse2, bookCategoryResponse3));

		List<ILoggingEvent> logsList = listAppender.list;
		boolean errorLogFound = logsList.stream()
			.anyMatch(event -> event.getLevel().equals(ch.qos.logback.classic.Level.ERROR)
				&& event.getFormattedMessage().contains("0 ~ 300 안에서 에러가 발생하였습니다."));

		assertTrue(errorLogFound);
	}

	@Test
	public void testElasticBookUpdate() {
		when(bookRedisRepository.isContent()).thenReturn(true);
		when(bookRedisRepository.getBookUpdateElasticBody()).thenReturn("bulk body");
		when(bookRestTemplate.sendBulk(anyString(), anyString())).thenReturn(ResponseEntity.ok("Success"));

		bookService.elasticBookUpdate();

		verify(bookRestTemplate, times(1)).sendBulk(anyString(), eq("3runner_book_alias"));
	}

}