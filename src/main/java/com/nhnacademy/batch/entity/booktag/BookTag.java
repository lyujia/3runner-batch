package com.nhnacademy.batch.entity.booktag;

import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.tag.Tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class BookTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Setter
	@ManyToOne
	private Book book;

	@Setter
	@ManyToOne
	private Tag tag;

	public BookTag(Book book, Tag tag) {
		this.book = book;
		this.tag = tag;
	}
}
