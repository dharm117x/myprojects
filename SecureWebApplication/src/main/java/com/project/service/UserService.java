package com.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.dto.UserForm;
import com.project.entity.User;

public interface UserService {

	Page<User> listUsers(String keyword, Pageable pageable);

	User getUser(Long id);

	void saveUser(UserForm form);

	void deleteUser(Long id);

}
