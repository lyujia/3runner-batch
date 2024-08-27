package com.nhnacademy.batch.batch.book.book.repository;

import org.springframework.http.ResponseEntity;

public interface BookRestTemplate {

	void saveNewIndex(String body, String indexName);

	ResponseEntity<String> sendBulk(String body, String indexName);

	void sendAlias(String body);
}
