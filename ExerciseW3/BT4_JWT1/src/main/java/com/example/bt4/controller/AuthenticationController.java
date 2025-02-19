package com.example.bt4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bt4.entity.User;
import com.example.bt4.model.LoginResponse;
import com.example.bt4.model.LoginUserModel;
import com.example.bt4.model.RegisterUserModel;
import com.example.bt4.service.AuthenticationService;
import com.example.bt4.service.JwtService;

import jakarta.transaction.Transactional;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	private final JwtService jwtService;
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/signup")
	@Transactional
	public ResponseEntity<User> register(@RequestBody RegisterUserModel registerUser) {
		User registeredUser = authenticationService.signup(registerUser);
		return ResponseEntity.ok(registeredUser);
	}
	
	@PostMapping(path="/login")
	@Transactional
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserModel loginUser) {
		User authenticationUser = authenticationService.authenticate(loginUser);
		
		String jwtToken = jwtService.generateToken(authenticationUser);
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(loginResponse);
	}
}
