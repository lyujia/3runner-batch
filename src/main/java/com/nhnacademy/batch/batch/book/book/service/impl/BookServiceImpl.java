package com.nhnacademy.batch.batch.book.book.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.batch.batch.book.book.repository.BookDocumentRepository;
import com.nhnacademy.batch.batch.book.book.repository.BookRedisRepository;
import com.nhnacademy.batch.batch.book.book.repository.BookRepository;
import com.nhnacademy.batch.batch.book.book.repository.BookRestTemplate;
import com.nhnacademy.batch.batch.book.book.repository.JsonFileLoadRepository;
import com.nhnacademy.batch.batch.book.book.response.BookDocument;
import com.nhnacademy.batch.batch.book.book.service.BookService;
import com.nhnacademy.batch.batch.book.category.repository.CategoryCustomRepository;
import com.nhnacademy.batch.batch.book.category.response.BookCategoryResponse;
import com.nhnacademy.batch.batch.book.tag.repository.TagCustomRepository;
import com.nhnacademy.batch.batch.book.tag.response.BookTagResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;
	private final JsonFileLoadRepository jsonFileLoadRepository;
	private final BookRestTemplate bookRestTemplate;
	private final BookDocumentRepository bookDocumentRepository;
	private final BookRedisRepository bookRedisRepository;
	private final TagCustomRepository tagCustomRepository;
	private final CategoryCustomRepository categoryCustomRepository;

	private final static String DEFAULT_BOOK_ALIAS = "3runner_book_alias";
	private final static String DEFAULT_BOOK_INDEX = "3runner_book_";
	private final static long BOOK_SAVE_SIZE = 300;

	/**
	 * db의 모든 책들을 elastic 으로 밀어 넣기
	 *
	 */
	@Override
	public void allBookToElasticSearchFromDB() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		String today = currentDate.format(formatter);
		String yesterday = currentDate.minusDays(1).format(formatter);

		String indexName = DEFAULT_BOOK_INDEX + today;
		String yesterdayIndexName = DEFAULT_BOOK_INDEX + yesterday;

		String indexSettingBody = jsonFileLoadRepository.jsonFileLoad("book-document.json");
		bookRestTemplate.saveNewIndex(indexSettingBody, indexName);

		log.info("새로운 index {} 생성", indexName);

		long maxBookId = bookRepository.maxBookId();

		long startBookId = 0;
		while (startBookId <= maxBookId) {
			try {
				String bulkBody = splitSendBulkBody(startBookId, indexName);
				bookRestTemplate.sendBulk(bulkBody, indexName);
				log.info("{} ~ {} 범위 저장 성공", startBookId, (startBookId + BOOK_SAVE_SIZE));
			} catch (Exception e) {
				log.error("{} ~ {} 안에서 에러가 발생하였습니다. ", startBookId, (startBookId + BOOK_SAVE_SIZE));
			} finally {
				startBookId += BOOK_SAVE_SIZE;
			}
		}
		log.info("bulk 완료");

		String aliasBody = bookDocumentRepository.changeAlias(yesterdayIndexName, indexName);
		bookRestTemplate.sendAlias(aliasBody);

		log.info("alias index 교체 완료");

	}

	@Override
	public void elasticBookUpdate() {
		if (bookRedisRepository.isContent()) {
			log.info("elastic search 변경 내용이 감지 되었습니다.");
			String bulkBody = bookRedisRepository.getBookUpdateElasticBody();
			ResponseEntity<String> response = bookRestTemplate.sendBulk(bulkBody, DEFAULT_BOOK_ALIAS);
			log.info("elastic search 변경을 완료 했습니다. 내용 : {}", response.getBody());
		}

	}

	/**
	 * BOOK_SAVE_SIZE 식 끊어서 db 에서 책 정보를 가져와 elastic 에 저장
	 * @param startBookId 시작하는 bookId
	 * @param indexName 저장하는 index 이름
	 * @return bulk 쿼리의 body
	 */
	private String splitSendBulkBody(long startBookId, String indexName) {
		long finishBookId = startBookId + BOOK_SAVE_SIZE;
		List<BookDocument> bookDocumentList = bookRepository.bookDocumentList(startBookId, finishBookId);
		List<BookTagResponse> bookTagResponseList = tagCustomRepository.bookCategories(startBookId, finishBookId);
		List<BookCategoryResponse> bookCategoryResponseList = categoryCustomRepository.bookCategories(startBookId,
			finishBookId);

		HashMap<Long, BookDocument> bookDocumentHashMap = new HashMap<>();
		for (BookDocument bookDocument : bookDocumentList) {
			bookDocumentHashMap.put(bookDocument.getId(), bookDocument);
		}
		for (BookTagResponse bookTagResponse : bookTagResponseList) {
			bookDocumentHashMap.get(bookTagResponse.getBookId()).addTag(bookTagResponse.getName());
		}
		for (BookCategoryResponse bookCategoryResponse : bookCategoryResponseList) {
			bookDocumentHashMap.get(bookCategoryResponse.getBookId()).addCategory(bookCategoryResponse.getName());
		}
		return bookDocumentRepository.makeBulkBody(bookDocumentList, indexName);
	}
}
