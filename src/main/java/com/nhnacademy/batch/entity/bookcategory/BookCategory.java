package com.nhnacademy.batch.entity.bookcategory;

import com.nhnacademy.batch.entity.book.Book;
import com.nhnacademy.batch.entity.category.Category;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class BookCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@Setter
	private Book book;

	@ManyToOne
	@Setter
	private Category category;

}
