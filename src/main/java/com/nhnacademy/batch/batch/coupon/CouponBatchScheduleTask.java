package com.nhnacademy.batch.batch.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponBatchScheduleTask {
	private final JobLauncher jobLauncher;
	private final Job couponJob;

	@Scheduled(cron = "0 0 0 * * ?")
	public void issueCouponTime() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
			jobLauncher.run(couponJob, jobParameters);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
