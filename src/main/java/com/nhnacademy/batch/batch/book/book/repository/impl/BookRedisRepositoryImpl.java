package com.nhnacademy.batch.batch.book.book.repository.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.nhnacademy.batch.batch.book.book.repository.BookRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookRedisRepositoryImpl implements BookRedisRepository {
	private final RedisTemplate<String, Object> redisTemplate;
	private static final String DEFAULT_BOOK_KEY = "bookDocument";

	@Override
	public String getBookUpdateElasticBody() {
		List<Object> stringList = redisTemplate.opsForHash().values(DEFAULT_BOOK_KEY);
		StringBuilder sb = new StringBuilder();

		if (!stringList.isEmpty()) {
			for (Object string : stringList) {
				String str = (String)string;
				sb.append(str).append("\n");
			}
		}
		Set<Object> keys = redisTemplate.opsForHash().keys(DEFAULT_BOOK_KEY);

		if (!keys.isEmpty()) {
			redisTemplate.opsForHash().delete(DEFAULT_BOOK_KEY, keys.toArray());
		}

		return sb.toString();
	}

	@Override
	public boolean isContent() {
		List<Object> stringList = redisTemplate.opsForHash().values(DEFAULT_BOOK_KEY);
		return !stringList.isEmpty();
	}
}
