package com.app.loginsystem.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.loginsystem.entity.SecurityUser;
import com.app.loginsystem.repo.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	public JpaUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;  
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository
				.findByUsername(username)
				.map(SecurityUser::new)
				.orElseThrow(()->new UsernameNotFoundException("not found"));
				
	}

}
