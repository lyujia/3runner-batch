package com.nhnacademy.batch.batch.member;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTask {
	private final JobLauncher jobLauncher;
	private final Job job1;
	private final Job job2;

	@Scheduled(cron = "1 30 0 * * ?")
	public void reportCurrentTime() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
			jobLauncher.run(job1, jobParameters);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Scheduled(cron = "1 0 0 * * ?")
	public void runMemberLevelUpgradeJob() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
			jobLauncher.run(job2, jobParameters);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
