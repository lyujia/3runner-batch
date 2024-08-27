package com.nhnacademy.batch.batch.book.book.exception;

public class ElasticBulkMadeException extends RuntimeException {
	public ElasticBulkMadeException() {
		super("엘라스틱 서치 bulk 의 body 만들기 오류");
	}
}
