package com.example.bt4.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.bt4.entity.User;
import com.example.bt4.repository.UserRepository;

public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public List<User> allUsers() {
		List<User> users = new ArrayList<>();
		
		userRepository.findAll().forEach(users::add);
		
		return users;
	}
}
