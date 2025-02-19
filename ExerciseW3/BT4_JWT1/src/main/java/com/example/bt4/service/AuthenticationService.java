package com.example.bt4.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bt4.entity.User;
import com.example.bt4.model.LoginUserModel;
import com.example.bt4.model.RegisterUserModel;
import com.example.bt4.repository.UserRepository;

@Service
public class AuthenticationService {
	private final UserRepository userRepository;
	
	private final AuthenticationManager authenticationManager;
	
	private final PasswordEncoder passwordEncoder;
	
	public AuthenticationService(
			UserRepository userRepository,
			AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder
			) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}
	
	public User signup(RegisterUserModel input) {
		User user = new User();
		user.setFullName(input.getFullName());
		user.setEmail(input.getEmail());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		
		return userRepository.save(user);
	}
	
	public User authenticate(LoginUserModel input) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
				);
		return userRepository.findByEmail(input.getEmail())
				.orElseThrow();
	}
}
