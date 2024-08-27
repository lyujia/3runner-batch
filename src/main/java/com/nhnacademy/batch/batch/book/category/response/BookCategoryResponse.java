package com.nhnacademy.batch.batch.book.category.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookCategoryResponse {
	private Long bookId;
	private String name;

	public BookCategoryResponse(Long bookId, String name) {
		this.bookId = bookId;
		this.name = name;
	}
}
