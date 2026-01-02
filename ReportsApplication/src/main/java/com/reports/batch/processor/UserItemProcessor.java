package com.reports.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;

import com.reports.batch.entity.User;

@Component
public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException(
                "Invalid email for id=" + user.getId()
            );
        }
        return user;
    }
}
