package com.pickSlip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickSlip.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}