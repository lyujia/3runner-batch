package com.nhnacademy.batch.batch.book.tag.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookTagResponse {
	private long bookId;
	private String name;

	public BookTagResponse(long bookId, String name) {
		this.bookId = bookId;
		this.name = name;
	}
}
