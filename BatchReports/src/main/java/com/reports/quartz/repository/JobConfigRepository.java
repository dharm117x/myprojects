package com.reports.quartz.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.reports.quartz.model.JobConfig;

public interface JobConfigRepository extends JpaRepository<JobConfig, Long> {
    JobConfig findByJobNameAndJobGroup(String name, String group);
}
