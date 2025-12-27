package com.reports.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class SampleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("Executing job: " + context.getJobDetail().getKey());
    }
}
