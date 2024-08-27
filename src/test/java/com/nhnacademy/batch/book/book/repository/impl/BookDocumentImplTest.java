package com.nhnacademy.batch.book.book.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nhnacademy.batch.batch.book.book.exception.ElasticBulkMadeException;
import com.nhnacademy.batch.batch.book.book.repository.impl.BookDocumentImpl;
import com.nhnacademy.batch.batch.book.book.response.BookDocument;

public class BookDocumentImplTest {

	private BookDocumentImpl bookDocumentRepository;

	@BeforeEach
	public void setUp() {
		bookDocumentRepository = new BookDocumentImpl();
	}

	@Test
	public void testMakeBulkBodySuccess() {
		List<BookDocument> bookDocumentList = Arrays.asList(
			new BookDocument(1L, "Title 1", 100, 90, "Author 1", "http://example.com/book1.jpg", "Publisher 1"),
			new BookDocument(2L, "Title 2", 200, 180, "Author 2", "http://example.com/book2.jpg", "Publisher 2")
		);
		String indexName = "test_index";

		String bulkBody = bookDocumentRepository.makeBulkBody(bookDocumentList, indexName);

		String expectedBulkBody = "{ \"index\": { \"_index\": \"test_index\", \"_id\":\"1\" } }\n"
			+ "{\"id\":1,\"title\":\"Title 1\",\"price\":100,\"sellingPrice\":90,\"author\":\"Author 1\",\"thumbnail\":\"http://example.com/book1.jpg\",\"publisher\":\"Publisher 1\",\"tagList\":[],\"categoryList\":[]}\n"
			+ "{ \"index\": { \"_index\": \"test_index\", \"_id\":\"2\" } }\n"
			+ "{\"id\":2,\"title\":\"Title 2\",\"price\":200,\"sellingPrice\":180,\"author\":\"Author 2\",\"thumbnail\":\"http://example.com/book2.jpg\",\"publisher\":\"Publisher 2\",\"tagList\":[],\"categoryList\":[]}\n";

		assertEquals(expectedBulkBody, bulkBody);
	}

	@Test
	public void testMakeBulkBodyJsonProcessingException() {
		List<BookDocument> bookDocumentList = Arrays.asList(new BookDocument() {
			@Override
			public String getTitle() {
				throw new RuntimeException("Test exception");
			}
		});
		String indexName = "test_index";

		assertThrows(ElasticBulkMadeException.class, () -> {
			bookDocumentRepository.makeBulkBody(bookDocumentList, indexName);
		});
	}

	@Test
	public void testChangeAlias() {
		String yesterdayIndexName = "yesterday_index";
		String todayIndexName = "today_index";

		String aliasBody = bookDocumentRepository.changeAlias(yesterdayIndexName, todayIndexName);

		String expectedAliasBody = "{\n"
			+ "  \"actions\": [\n"
			+ "    {\n"
			+ "      \"remove\": {\n"
			+ "        \"index\": \"yesterday_index\",\n"
			+ "        \"alias\": \"3runner_book_alias\"\n"
			+ "      }\n"
			+ "    },\n"
			+ "    {\n"
			+ "      \"add\": {\n"
			+ "        \"index\": \"today_index\",\n"
			+ "        \"alias\": \"3runner_book_alias\"\n"
			+ "      }\n"
			+ "    }\n"
			+ "  ]\n"
			+ "}";

		assertEquals(expectedAliasBody, aliasBody);
	}
}
