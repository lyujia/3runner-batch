package com.nhnacademy.batch.batch.book.tag.repository;

import java.util.List;

import com.nhnacademy.batch.batch.book.tag.response.BookTagResponse;

public interface TagCustomRepository {
	List<BookTagResponse> bookCategories(long minBookId, long maxBookId);
}
