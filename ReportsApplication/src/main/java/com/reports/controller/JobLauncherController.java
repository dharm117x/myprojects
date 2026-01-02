package com.reports.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
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
                         @RequestParam String toDate, RedirectAttributes rd) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addString("fromDate", fromDate)
                .addString("toDate", toDate)
                .addLong("runId", System.currentTimeMillis()) // unique
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(userDbJob, params);
        BatchStatus status = jobExecution.getStatus();
		if (status == BatchStatus.COMPLETED) {
			rd.addFlashAttribute("successMessage", "DB Job completed successfully.");
		} else if (status == BatchStatus.FAILED) {
			rd.addFlashAttribute("errorMessage", "DB Job failed with exceptions. Check logs for details.");
			jobExecution.getAllFailureExceptions().forEach(Throwable::printStackTrace);
		}
		
        return "redirect:/";
    }
    
    @PostMapping("/file-job")
	public String runFileJob(@RequestParam String fileName, RedirectAttributes rd) throws Exception {

		JobParameters params = new JobParametersBuilder()
				.addString("outputFile", fileName +"-"+ System.currentTimeMillis() + ".txt")
				.addLong("runId", System.currentTimeMillis()) // unique
				.toJobParameters();

        JobExecution jobExecution = jobLauncher.run(userFileJob, params);
        BatchStatus status = jobExecution.getStatus();
		if (status == BatchStatus.COMPLETED) {
			rd.addFlashAttribute("successMessage", "File Job completed successfully.");
		} else if (status == BatchStatus.FAILED) {
			rd.addFlashAttribute("errorMessage", "File Job failed with exceptions. Check logs for details.");
			jobExecution.getAllFailureExceptions().forEach(Throwable::printStackTrace);
		}

		return "redirect:/";
	}
}
