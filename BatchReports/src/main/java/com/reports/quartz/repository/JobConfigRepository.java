package com.reports.quartz.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.reports.quartz.model.JobConfig;

public interface JobConfigRepository extends JpaRepository<JobConfig, Long> {
	JobConfig findByJobNameAndJobGroup(String name, String group);

	@Modifying
	@Transactional
	@Query("UPDATE JobConfig j SET j.startTime = :startTime WHERE j.jobName = :name AND j.jobGroup = :group")
	int updateStartTime(@Param("name") String name, @Param("group") String group,
			@Param("startTime") LocalDateTime startTime);

	@Modifying
	@Transactional
	@Query("UPDATE JobConfig j SET j.endTime = :endTime WHERE j.jobName = :name AND j.jobGroup = :group")
	int updateEndTime(@Param("name") String name, @Param("group") String group,
			@Param("endTime") LocalDateTime endTime);
}
