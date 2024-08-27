package com.nhnacademy.batch.batch.book.book.repository;

import java.util.List;

import com.nhnacademy.batch.batch.book.book.response.BookDocument;

public interface BookDocumentRepository {
	String makeBulkBody(List<BookDocument> bookDocumentList, String indexName);

	String changeAlias(String yesterdayIndexName, String todayIndexName);
}
