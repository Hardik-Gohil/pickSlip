package com.pickSlip.service;

import com.pickSlip.entity.User;

public interface UserService {
	void save(User user);

	User findByUsername(String username);
}