package com.reports.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class JobLauncherController {

	
    private final JobLauncher jobLauncher;
    private final Job userDbJob;
    private final Job userFileJob;
    
	public JobLauncherController(JobLauncher jobLauncher, Job userDbJob, Job userFileJob) {
		this.jobLauncher = jobLauncher;
		this.userDbJob = userDbJob;
		this.userFileJob = userFileJob;
	}
    

    @PostMapping("/db-job")
    public String runJob(@RequestParam String fromDate,
                         @RequestParam String toDate) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addString("fromDate", fromDate)
                .addString("toDate", toDate)
                .addLong("runId", System.currentTimeMillis()) // unique
                .toJobParameters();

        jobLauncher.run(userDbJob, params);
        return "Job Started";
    }
    
    @PostMapping("/file-job")
	public String runFileJob(@RequestParam String fileName) throws Exception {

		JobParameters params = new JobParametersBuilder()
				.addString("outputFile", fileName +"-"+ System.currentTimeMillis() + ".txt")
				.addLong("runId", System.currentTimeMillis()) // unique
				.toJobParameters();

		jobLauncher.run(userFileJob, params);
		return "File Job Started";
	}
}
