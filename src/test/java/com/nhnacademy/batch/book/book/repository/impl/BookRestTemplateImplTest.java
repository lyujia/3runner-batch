package com.nhnacademy.batch.book.book.repository.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.nhnacademy.batch.batch.book.book.repository.impl.BookRestTemplateImpl;

@ExtendWith(MockitoExtension.class)
class BookRestTemplateImplTest {

	@InjectMocks
	private BookRestTemplateImpl bookRestTemplate;

	@Mock
	private RestTemplate restTemplate;

	@Value("${elasticsearch.key}")
	private String apiKey = "testApiKey";

	@Value("${elasticsearch.url}")
	private String elasticSearchUrl = "localhost:9200";

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(bookRestTemplate, "apiKey", apiKey);
		ReflectionTestUtils.setField(bookRestTemplate, "elasticSearchUrl", elasticSearchUrl);
	}

	@Test
	void testSaveNewIndex() {
		// given
		String body = "{\"settings\": {\"number_of_shards\": 1}}";
		String indexName = "test-index";
		String url = "http://" + elasticSearchUrl + "/" + indexName;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "ApiKey " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		// when
		when(restTemplate.exchange(eq(url), eq(HttpMethod.PUT), eq(entity), eq(String.class))).thenReturn(
			new ResponseEntity<>(HttpStatus.OK));

		// execute
		bookRestTemplate.saveNewIndex(body, indexName);

		// then
		verify(restTemplate, times(1)).exchange(eq(url), eq(HttpMethod.PUT), eq(entity), eq(String.class));
	}

	@Test
	void testSendBulk() {
		// given
		String body = "{\"index\": {\"_index\": \"test-index\", \"_id\": \"1\"}}\n{\"field1\": \"value1\"}";
		String indexName = "test-index";
		String url = "http://" + elasticSearchUrl + "/" + indexName + "/_bulk";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "ApiKey " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		// when
		when(restTemplate.exchange(eq(url), eq(HttpMethod.POST), eq(entity), eq(String.class))).thenReturn(
			new ResponseEntity<>(HttpStatus.OK));

		// execute
		ResponseEntity<String> response = bookRestTemplate.sendBulk(body, indexName);

		// then
		verify(restTemplate, times(1)).exchange(eq(url), eq(HttpMethod.POST), eq(entity), eq(String.class));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void testSendAlias() {
		// given
		String body = "{\"actions\": [{\"add\": {\"index\": \"test-index\", \"alias\": \"test-alias\"}}]}";
		String url = "http://" + elasticSearchUrl + "/_aliases";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "ApiKey " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		// when
		when(restTemplate.exchange(eq(url), eq(HttpMethod.POST), eq(entity), eq(String.class))).thenReturn(
			new ResponseEntity<>(HttpStatus.OK));

		// execute
		bookRestTemplate.sendAlias(body);

		// then
		verify(restTemplate, times(1)).exchange(eq(url), eq(HttpMethod.POST), eq(entity), eq(String.class));
	}
}
