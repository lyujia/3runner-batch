package com.nhnacademy.batch.batch.book.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nhnacademy.batch.entity.book.Book;

public interface BookRepository extends JpaRepository<Book, Long>, BookCustomRepository {

	@Query("select max(b.id) from Book b")
	Long maxBookId();
}
