package com.nhnacademy.batch.batch.book.category.repository;

import java.util.List;

import com.nhnacademy.batch.batch.book.category.response.BookCategoryResponse;

public interface CategoryCustomRepository {

	List<BookCategoryResponse> bookCategories(long minBookId, long maxBookId);
}
