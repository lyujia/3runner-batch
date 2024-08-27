package com.nhnacademy.batch.batch.book.book.repository;

import java.util.List;

import com.nhnacademy.batch.batch.book.book.response.BookDocument;

public interface BookCustomRepository {
	List<BookDocument> bookDocumentList(long minBookId, long maxBookId);
}
