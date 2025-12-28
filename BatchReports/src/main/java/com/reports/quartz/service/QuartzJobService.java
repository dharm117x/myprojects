package com.reports.quartz.service;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.stereotype.Service;

import com.reports.core.mapper.ConfigMapper;
import com.reports.quartz.dto.JobConfigDto;
import com.reports.quartz.listener.QuartzJobListener;
import com.reports.quartz.model.JobConfig;
import com.reports.quartz.repository.JobConfigRepository;

@Service
public class QuartzJobService {

	private final Scheduler scheduler;
	private final ConfigMapper configMapper;
	private final JobConfigRepository jobRepo;

	public QuartzJobService(Scheduler scheduler, ConfigMapper configMapper, JobConfigRepository jobRepo) {
		this.scheduler = scheduler;
		this.configMapper = configMapper;
		this.jobRepo = jobRepo;
	}

	public void scheduleJob(JobConfigDto job) throws Exception {
		JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(job.getJobClass()))
				.withIdentity(job.getJobName(), job.getJobGroup()).withDescription(job.getDescription()).build();

		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName() + "_trigger", job.getJobGroup())
				.withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).build();

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.getListenerManager().addJobListener(new QuartzJobListener(jobRepo));
		job.setStatus("ACTIVE");
		JobConfig entity = configMapper.toEntity(job);
		jobRepo.save(entity);
	}

	public void updateJob(JobConfigDto job) throws Exception {
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName() + "_trigger", job.getJobGroup());

		CronTrigger newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
				.withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).build();

		scheduler.rescheduleJob(triggerKey, newTrigger);
		JobConfig entity = jobRepo.findByJobNameAndJobGroup(job.getJobName(), job.getJobGroup());
		entity.setCronExpression(job.getCronExpression());
		jobRepo.save(entity);

	}

	public void pauseJob(JobConfigDto job) throws Exception {
		scheduler.pauseJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
		JobConfig entity = jobRepo.findByJobNameAndJobGroup(job.getJobName(), job.getJobGroup());
		entity.setStatus("PAUSED");
		jobRepo.save(entity);

	}

	public void resumeJob(JobConfigDto job) throws Exception {
		scheduler.resumeJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
		JobConfig entity = jobRepo.findByJobNameAndJobGroup(job.getJobName(), job.getJobGroup());
		entity.setStatus("ACTIVE");
		jobRepo.save(entity);

	}

	public void deleteJob(JobConfigDto job) throws Exception {
		scheduler.deleteJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
		JobConfig entity = jobRepo.findByJobNameAndJobGroup(job.getJobName(), job.getJobGroup());
		jobRepo.delete(entity);
	}

	public List<JobConfigDto> getAllJobs() {
		List<JobConfigDto> list = jobRepo.findAll().stream().map(model -> configMapper.toDto(model)).toList();
		return list;
	}

	public JobConfigDto getJobDetails(String jobName, String jobGroup) {
		JobConfig jobConfig = jobRepo.findByJobNameAndJobGroup(jobName, jobGroup);
		return  jobConfig != null
				? configMapper.toDto(jobConfig)
				: null;
	}
}
