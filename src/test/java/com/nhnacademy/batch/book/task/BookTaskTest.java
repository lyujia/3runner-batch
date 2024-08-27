package com.nhnacademy.batch.book.task;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.nhnacademy.batch.batch.book.book.service.BookService;
import com.nhnacademy.batch.batch.book.task.BookTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
@EnableScheduling
class BookTaskTest {

	@Mock
	private BookService bookService;

	@InjectMocks
	private BookTask bookTask;

	@BeforeEach
	void setUp() {
		bookTask = new BookTask(bookService); // 새 BookTask 인스턴스를 생성하여 모든 변수를 초기화합니다.
	}

	@Test
	void testAllBookElasticSearchFromDB() throws Exception {
		// given
		// when
		bookTask.allBookElasticSearchFromDB();

		// then
		verify(bookService, times(1)).allBookToElasticSearchFromDB();
	}

	@Test
	void testBookElasticSearchChecking() throws Exception {
		// given
		bookTask.changeBookPlaying(false);

		// when
		bookTask.bookElasticSearchChecking();

		// then
		verify(bookService, times(1)).elasticBookUpdate();
	}

	@Test
	void notTestBookElasticSearchChecking() throws Exception {
		// given
		bookTask.changeBookPlaying(true);

		// when
		bookTask.bookElasticSearchChecking();

		// then
		verify(bookService, times(0)).elasticBookUpdate();
	}
}
