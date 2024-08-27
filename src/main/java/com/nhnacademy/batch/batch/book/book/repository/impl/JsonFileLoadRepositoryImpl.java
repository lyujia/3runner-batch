package com.nhnacademy.batch.batch.book.book.repository.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.batch.batch.book.book.repository.JsonFileLoadRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JsonFileLoadRepositoryImpl implements JsonFileLoadRepository {

	private final static String DEFAULT_FILE_NAME = "elastic/";

	/**
	 * 엘라스틱 서치의 인덱스 설정 값을 가져오기 위해 사용
	 * json 파일을 불러와 String 으로 변환
	 * @param fileName 가져올 파일
	 * @return String
	 */
	@Override
	public String jsonFileLoad(String fileName) {
		try {
			ClassPathResource resource = new ClassPathResource(DEFAULT_FILE_NAME + fileName);

			// InputStream을 열고 ObjectMapper를 사용하여 JSON을 Java 객체로 변환
			InputStream inputStream = resource.getInputStream();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(inputStream);

			// JsonNode를 String으로 변환
			String jsonString = objectMapper.writeValueAsString(jsonNode);

			// 결과 출력
			System.out.println(jsonString);

			return jsonString;

		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return "파일 불러오기 실패";
	}
}
