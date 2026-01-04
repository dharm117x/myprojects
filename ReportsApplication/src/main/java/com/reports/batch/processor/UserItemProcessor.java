package com.reports.batch.processor;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;

import com.reports.batch.dto.UserDto;
import com.reports.batch.entity.User;

@Component
public class UserItemProcessor implements ItemProcessor<User, UserDto> {

	private Long batchNo;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.batchNo = stepExecution.getJobExecution().getId();// Using JobExecution ID as batch number
	}

	@Override
	public UserDto process(User user) {
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			throw new ValidationException("Invalid email for id=" + user.getId());
		}
		if (user.getEmail() != null && user.getEmail().toLowerCase().contains("test")) {
			return null; // filtered, not written
		}

		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setBatchNo(batchNo.intValue());
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		userDto.setCreatedDate(user.getCreatedDate());
		userDto.setUpdatedDate(user.getUpdatedDate());

		return userDto;
	}
}
