package com.project.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.UserForm;
import com.project.entity.User;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.UserRepository;
import com.project.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    @Override
    public Page<User> listUsers(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return userRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void saveUser(UserForm form) {
        User user = (form.getId() != null)
                ? getUser(form.getId())
                : new User();

        user.setUsername(form.getEmail());
        user.setPassword(form.getPasswrod());
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setRole(form.getRole());

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
