package com.example.bt3.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bt3.entity.UserInfo;
import com.example.bt3.model.UserInfoUserDetails;
import com.example.bt3.repository.UserInfoRepository;

@Service
public class UserInfoService implements UserDetailsService{
	@Autowired
	private UserInfoRepository userRepo;
	
	public UserInfoService(UserInfoRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		// tìm user trong database
		Optional<UserInfo> user = userRepo.findUserByName(username);

		return user.map(UserInfoUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("user not found: " + username));
	}

}
