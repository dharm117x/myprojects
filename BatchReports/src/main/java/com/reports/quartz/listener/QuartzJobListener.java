package com.reports.quartz.listener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reports.quartz.repository.JobConfigRepository;


@Component
public class QuartzJobListener extends JobListenerSupport {

	private final JobConfigRepository configRepository;

	@Autowired
	public QuartzJobListener(JobConfigRepository configRepository) {
		this.configRepository = configRepository;
	}

    public String getName() { return "GlobalJobListener"; }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
    	Date fireTime = context.getFireTime();
    	JobDetail jobDetail = context.getJobDetail();
    	LocalDateTime startTime = fireTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    	configRepository.updateStartTime(jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), startTime);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        long duration = context.getJobRunTime();
        Date fireTime = context.getFireTime();
        LocalDateTime endTime = fireTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusNanos(duration * 1_000_000);
    	JobDetail jobDetail = context.getJobDetail();
    	configRepository.updateEndTime(jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), endTime);
    }

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// OK
	}
}