package com.nhnacademy.batch.global.exceptionHandler;

import lombok.Builder;

@Builder
public record ErrorResponseForm(String title, int status, String timestamp) {
}