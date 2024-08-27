package com.nhnacademy.batch.book.book.repository.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.nhnacademy.batch.batch.book.book.repository.impl.BookRedisRepositoryImpl;

class BookRedisRepositoryImplTest {

	@InjectMocks
	private BookRedisRepositoryImpl bookRedisRepository;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private HashOperations<String, Object, Object> hashOperations;

	private static final String DEFAULT_BOOK_KEY = "bookDocument";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(redisTemplate.opsForHash()).thenReturn(hashOperations);
	}

	@Test
	void testGetBookUpdateElasticBody() {
		// given
		List<Object> mockValues = List.of("Book1", "Book2");
		Set<Object> mockKeys = Set.of("Key1", "Key2");

		when(hashOperations.values(DEFAULT_BOOK_KEY)).thenReturn(mockValues);
		when(hashOperations.keys(DEFAULT_BOOK_KEY)).thenReturn(mockKeys);

		// when
		String result = bookRedisRepository.getBookUpdateElasticBody();

		// then
		assertThat(result).isEqualTo("Book1\nBook2\n");
		verify(hashOperations, times(1)).values(DEFAULT_BOOK_KEY);
		verify(hashOperations, times(1)).keys(DEFAULT_BOOK_KEY);
		verify(hashOperations, times(1)).delete(DEFAULT_BOOK_KEY, mockKeys.toArray());
	}

	@Test
	void testIsContent() {
		// given
		List<Object> mockValues = List.of("Book1", "Book2");

		when(hashOperations.values(DEFAULT_BOOK_KEY)).thenReturn(mockValues);

		// when
		boolean result = bookRedisRepository.isContent();

		// then
		assertThat(result).isTrue();
		verify(hashOperations, times(1)).values(DEFAULT_BOOK_KEY);
	}

	@Test
	void testIsContentEmpty() {
		// given
		List<Object> mockValues = List.of();

		when(hashOperations.values(DEFAULT_BOOK_KEY)).thenReturn(mockValues);

		// when
		boolean result = bookRedisRepository.isContent();

		// then
		assertThat(result).isFalse();
		verify(hashOperations, times(1)).values(DEFAULT_BOOK_KEY);
	}
}
