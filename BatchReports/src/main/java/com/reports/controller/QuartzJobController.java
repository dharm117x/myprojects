package com.reports.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reports.quartz.dto.JobConfigDto;
import com.reports.quartz.service.QuartzJobService;

@Controller
@RequestMapping("/quartz")
public class QuartzJobController {

	private final QuartzJobService jobService;

	public QuartzJobController(QuartzJobService jobService) {
		this.jobService = jobService;
	}

	@GetMapping
	public String listJobs(Model model) {
		List<JobConfigDto> allJobs = jobService.getAllJobs();
		model.addAttribute("jobs", allJobs);
		return "job-list";
	}

	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("job", new JobConfigDto());
		model.addAttribute("actionUrl", "/quartz/save");
		model.addAttribute("isDisabled", false);
		return "job-form";
	}

	@GetMapping("/edit")
	public String editJob(@RequestParam("jobName") String jobName, 
	                      @RequestParam("jobGroup") String jobGroup, 
	                      Model model) throws Exception {
	    JobConfigDto job = jobService.getJobDetails(jobName, jobGroup);
	    model.addAttribute("job", job);
	    model.addAttribute("actionUrl", "/quartz/update");
	    model.addAttribute("isDisabled", true);
	    return "job-form";
	}

	
	@PostMapping("/save")
	public String saveJob(@ModelAttribute("job") JobConfigDto job) throws Exception {
		jobService.scheduleJob(job);
		return "redirect:/quartz";
	}

	@PostMapping("/update")
	public String updateJob(@ModelAttribute("job") JobConfigDto job) throws Exception {
		jobService.updateJob(job);
		return "redirect:/quartz";
	}

	@PostMapping("/pause")
	public String pauseJob(@ModelAttribute("job") JobConfigDto job) throws Exception {
		jobService.pauseJob(job);
		return "redirect:/quartz";
	}

	@PostMapping("/resume")
	public String resumeJob(@ModelAttribute("job") JobConfigDto job) throws Exception {
		jobService.resumeJob(job);
		return "redirect:/quartz";
	}

	@PostMapping("/delete")
	public String deleteJob(@ModelAttribute("job") JobConfigDto job) throws Exception {
		jobService.deleteJob(job);
		return "redirect:/quartz";
	}

}
