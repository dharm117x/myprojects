package com.reports.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.reports.batch.dto.UserDto;
import com.reports.batch.entity.User;
import com.reports.batch.listener.UserStepListener;
import com.reports.batch.processor.UserItemProcessor;
import com.reports.batch.reader.UserItemAllDbReader;
import com.reports.batch.reader.UserItemFileReader;
import com.reports.batch.writer.UserFileItemWriter;
import com.reports.batch.writer.UserItemDbWriter;

@Configuration
public class BatchConfig {
	Logger logger = LoggerFactory.getLogger(BatchConfig.class);
	private final JobRepository jobRepository;
	private final PlatformTransactionManager txManager;

	@Autowired
	public BatchConfig(JobRepository jobRepository, PlatformTransactionManager txManager) {
		this.jobRepository = jobRepository;
		this.txManager = txManager;
	}

    @Bean
    public Job userDbJob(Step userStepFileToDatabse) {
    	logger.info("Creating userDbJob");
    	return new JobBuilder("user-db-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(userStepFileToDatabse)
                .build();
    }
    
    @Bean
    public Job userFileJob(Step userStepDatabseToFile) {
    	logger.info("Creating userFileJob");
    	return new JobBuilder("user-file-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(userStepDatabseToFile)
                .build();
    }
    
    
    @Bean
    public Step userStepFileToDatabse(UserItemFileReader reader,
                         UserItemProcessor processor,
                         UserItemDbWriter writer,
                         UserStepListener listener) {
    	logger.info("Creating userStepFileToDatabse step");
    	return	new StepBuilder("user-file-to-db-step", jobRepository)
                .<User, UserDto>chunk(500, txManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(listener)
                .faultTolerant()
                .skip(ValidationException.class)
                .skipLimit(10)
                .build();
    }
    
    @Bean
    public Step userStepDatabseToFile(UserItemAllDbReader reader,
                         UserItemProcessor processor,
                         UserFileItemWriter writer,
                         UserStepListener listener) {
    	logger.info("Creating userStepDatabseToFile step");
    	return	new StepBuilder("user-db-to-file-step", jobRepository)
                .<User, UserDto>chunk(500, txManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(listener)
                .faultTolerant()
                .skip(ValidationException.class)
                .skipLimit(10)
                .build();
    }


}
