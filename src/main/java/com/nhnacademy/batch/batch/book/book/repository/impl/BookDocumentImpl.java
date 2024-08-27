package com.nhnacademy.batch.batch.book.book.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.batch.batch.book.book.exception.ElasticBulkMadeException;
import com.nhnacademy.batch.batch.book.book.repository.BookDocumentRepository;
import com.nhnacademy.batch.batch.book.book.response.BookDocument;

@Repository
public class BookDocumentImpl implements BookDocumentRepository {
	/**
	 * 값을 넣기 위한 query 문 body
	 * @param bookDocumentList  책 리스트
	 * @param indexName            인덱스 이름
	 * @return query 문 body
	 */
	@Override
	public String makeBulkBody(List<BookDocument> bookDocumentList, String indexName) {
		try {
			StringBuilder bulkRequestBody = new StringBuilder();
			ObjectMapper objectMapper = new ObjectMapper();

			for (BookDocument bookDocument : bookDocumentList) {
				// Add index metadata
				bulkRequestBody.append("{ \"index\": { \"_index\": \"").append(indexName)
					.append("\", \"_id\":\"").append(bookDocument.getId()).append("\" } }\n");

				// Add the document data as JSON
				String jsonString = objectMapper.writeValueAsString(bookDocument);

				bulkRequestBody.append(jsonString).append("\n");
			}
			return bulkRequestBody.toString();

		} catch (JsonProcessingException e) {
			throw new ElasticBulkMadeException();
		}
	}

	/** 이전에 연결된 index 를 끊고 새로 만든 index 와 연결의 body
	 * @param yesterdayIndexName 이전에 사용한 index 이름
	 * @param todayIndexName    새롭게 만든 index 이름
	 * @return query 문 body
	 */
	@Override
	public String changeAlias(String yesterdayIndexName, String todayIndexName) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n"
			+ "  \"actions\": [\n"
			+ "    {\n"
			+ "      \"remove\": {\n"
			+ "        \"index\": \"").append(yesterdayIndexName).append("\",\n"
			+ "        \"alias\": \"3runner_book_alias\"\n"
			+ "      }\n"
			+ "    },\n"
			+ "    {\n"
			+ "      \"add\": {\n"
			+ "        \"index\": \"").append(todayIndexName).append("\",\n"
			+ "        \"alias\": \"3runner_book_alias\"\n"
			+ "      }\n"
			+ "    }\n"
			+ "  ]\n"
			+ "}");
		return sb.toString();
	}
}
