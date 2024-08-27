package com.nhnacademy.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class BatchServer {
	public static void main(String[] args) {
		SpringApplication.run(BatchServer.class, args);
	}

}
