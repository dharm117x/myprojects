package com.reports.batch.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.reports.batch.entity.User;
import com.reports.batch.repository.UserRepository;

@Component
public class UserItemDbWriter implements ItemWriter<User> {
	
	private final UserRepository userRepository;
	
	public UserItemDbWriter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	

	@Override
	@Transactional
	public void write(Chunk<? extends User> users) {
		userRepository.saveAll(users.getItems());
	}

}
