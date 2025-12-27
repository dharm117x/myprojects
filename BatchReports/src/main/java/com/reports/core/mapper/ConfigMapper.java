package com.reports.core.mapper;

import org.mapstruct.Mapper;

import com.reports.quartz.dto.JobConfigDto;
import com.reports.quartz.model.JobConfig;

@Mapper(componentModel = "spring")
public interface ConfigMapper {
	JobConfigDto toDto(JobConfig entity);
	JobConfig toEntity(JobConfigDto dto);
}
