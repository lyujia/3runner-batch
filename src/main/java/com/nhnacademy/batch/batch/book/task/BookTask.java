package com.nhnacademy.batch.batch.book.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nhnacademy.batch.batch.book.book.service.BookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookTask {
	private final BookService bookService;
	private static boolean allBookPlaying = false;

	public void changeBookPlaying(boolean allBookPlaying) {
		BookTask.allBookPlaying = allBookPlaying;
	}

	@Scheduled(cron = "0 0 2 * * ?")
	public void allBookElasticSearchFromDB() throws Exception {
		changeBookPlaying(true);
		bookService.allBookToElasticSearchFromDB();
		changeBookPlaying(false);
	}

	@Scheduled(fixedDelay = 60000)
	public void bookElasticSearchChecking() throws Exception {
		log.info("elastic search 변경한 내용을 감지 중입니다.");
		if (!allBookPlaying) {
			bookService.elasticBookUpdate();
		}
		log.info("elastic search 변경한 내용이 끝났습니다.");
	}
}
