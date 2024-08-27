package com.nhnacademy.batch.batch.book.book.repository.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.nhnacademy.batch.batch.book.book.repository.BookRestTemplate;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookRestTemplateImpl implements BookRestTemplate {

	@Value("${elasticsearch.key}")
	private String apiKey;

	@Value("${elasticsearch.url}")
	private String elasticSearchUrl;

	private final RestTemplate restTemplate;

	/**
	 * 새로운 인덱스를 생성하는 API를 보내는 메소드
	 *
	 * @param body       생성할 인덱스의 설정값
	 * @param indexName  인덱스 이름
	 */
	@Override
	public void saveNewIndex(String body, String indexName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "ApiKey " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		String url = "http://" + elasticSearchUrl + "/" + indexName;

		restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
	}

	/**
	 * bulk 쿼리 보내기
	 *
	 * @param body      쿼리 내용
	 * @param indexName 인덱스
	 */
	@Override
	public ResponseEntity<String> sendBulk(String body, String indexName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "ApiKey " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		String url = "http://" + elasticSearchUrl + "/" + indexName + "/_bulk";
		return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	}

	@Override
	public void sendAlias(String body) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "ApiKey " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		String url = "http://" + elasticSearchUrl + "/_aliases";
		restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	}

}