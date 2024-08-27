package com.nhnacademy.batch.entity.bookimage;

import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.bookimage.enums.BookImageType;
import com.nhnacademy.batch.entity.totalimage.TotalImage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private BookImageType type;

	@Setter
	@ManyToOne
	private Book book;

	@Setter
	@OneToOne(cascade = CascadeType.ALL)
	private TotalImage totalImage;

	public BookImage(BookImageType type, Book book, TotalImage totalImage) {
		this.type = type;
		this.book = book;
		this.totalImage = totalImage;
	}
}
