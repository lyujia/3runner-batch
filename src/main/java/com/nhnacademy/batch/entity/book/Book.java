package com.nhnacademy.batch.entity.book;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.batch.entity.bookcategory.BookCategory;
import com.nhnacademy.batch.entity.bookimage.BookImage;
import com.nhnacademy.batch.entity.booktag.BookTag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Size(min = 1, max = 50)
	@NotNull
	private String title;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;

	private ZonedDateTime publishedDate;

	@NotNull
	@Min(0)
	private int price;

	@NotNull
	@Min(0)
	private int quantity;

	@NotNull
	@Min(0)
	private int sellingPrice;

	@NotNull
	@Min(0)
	@Column(columnDefinition = "int default 0")
	private int viewCount;

	@NotNull
	private boolean packing;

	@NotNull
	@Size(min = 1, max = 50)
	private String author;

	@NotNull
	@Column(unique = true)
	@Size(min = 1, max = 20)
	private String isbn;

	@NotNull
	@Size(min = 1, max = 50)
	private String publisher;

	@NotNull
	private ZonedDateTime createdAt;

	//    public Book(long id, String title, String description, ZonedDateTime publishedDate, )
	//연결

	@Setter
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookCategory> bookCategoryList = new ArrayList<>();

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookTag> bookTagList = new ArrayList<>();

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookImage> bookImageList = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		this.createdAt = ZonedDateTime.now();
	}

}
