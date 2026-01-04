package com.reports.batch.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.reports.batch.dto.UserDto;
import com.reports.batch.entity.User;
import com.reports.batch.repository.UserRepository;

@Component
public class UserItemDbWriter implements ItemWriter<UserDto> {

	private final UserRepository userRepository;

	public UserItemDbWriter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public void write(Chunk<? extends UserDto> userDtos) {
		List<User> users = new ArrayList<>();
		userDtos.getItems().forEach(userDto -> {
			User user = new User();
			user.setId(userDto.getId());
			user.setName(userDto.getName());
			user.setEmail(userDto.getEmail());
			user.setCreatedDate(userDto.getCreatedDate());
			user.setUpdatedDate(userDto.getUpdatedDate());
			users.add(user);
		});
		userRepository.saveAll(users);
	}

}
