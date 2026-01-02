package com.reports.batch.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.reports.batch.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Page<User> findByCreatedDateBetween(LocalDate from, LocalDate to, Pageable pageable);

}
