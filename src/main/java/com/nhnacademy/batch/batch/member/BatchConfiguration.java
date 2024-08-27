package com.nhnacademy.batch.batch.member;

import com.nhnacademy.batch.entity.member.Member;
import com.nhnacademy.batch.entity.member.enums.Status;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job job1(JobRepository jobRepository, Step step1){
        return new JobBuilder("memberStatusJob", jobRepository).start(step1).build();
    }

    @Bean
    public Job job2(JobRepository jobRepository, Step step2){
        return new JobBuilder("memberGradeJob", jobRepository).start(step2).build();
    }

    @Bean
    public Step step1(JobRepository jobRepository){
        return new StepBuilder("statusUpdateStep", jobRepository)
                .<Member, Member>chunk(10, transactionManager)
                .reader(statusUpdateReader())
                .processor(statusUpdateProcessor())
                .writer(statusUpdateWriter())
                .build();
    }
    @Bean
    public Step step2(JobRepository jobRepository){
        return new StepBuilder("gradeUpdateStep",jobRepository)
            .<Member,Member>chunk(10,transactionManager)
            .reader(gradeUpdateReader())
            .processor(gradeUpdateProcessor())
            .writer(gradeUpdateWriter())
            .build();
    }
    @Bean
    public JpaPagingItemReader<Member> statusUpdateReader(){
        return new JpaPagingItemReaderBuilder<Member>()
                .name("jpapagingreader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT m FROM Member m ORDER BY m.id ASC")
                .build();
    }
    @Bean
    public ItemProcessor<Member, Member> statusUpdateProcessor(){
        return (member-> {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            long dayDifference = ChronoUnit.DAYS.between(member.getLastLoginDate(),now);
            if(dayDifference>=7){
                member.setStatus(Status.Inactive);
                return member;
            }else{
                return member;
            }
        });
    }
    @Bean
    public JpaItemWriter<Member> statusUpdateWriter(){
        JpaItemWriter<Member> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
    @Bean
    public JpaPagingItemReader<Member> gradeUpdateReader() {
        return new JpaPagingItemReaderBuilder<Member>()
            .name("gradeUpdateReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT m FROM Member m ORDER BY m.id ASC")
            .build();
    }

    @Bean
    public ItemProcessor<Member, Member> gradeUpdateProcessor() {
        return new GradeUpdateProcessor(entityManagerFactory.createEntityManager());
    }

    @Bean
    public JpaItemWriter<Member> gradeUpdateWriter() {
        JpaItemWriter<Member> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
