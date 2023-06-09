package com.app.loginsystem.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.loginsystem.dto.CreateUserRequest;
import com.app.loginsystem.entity.User;
import com.app.loginsystem.repo.UserRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Data
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	int flag=0;
	
	public boolean createUser(CreateUserRequest createUserRequest ) {
		Optional<User> userByName= userRepository.findByUsername(createUserRequest.getUsername());
		Optional<User> userByEmail= userRepository.findByEmail(createUserRequest.getEmail()); 
		
		if(userByName.isPresent()) {
			this.flag=1;
			return false;
			
		}else if (userByEmail.isPresent()) {
			this.flag=1;
			return false;	
			
		}else if(createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			User newUser= User.builder()
					.username(createUserRequest.getUsername())
					.email(createUserRequest.getEmail())
					.password(encoder.encode(createUserRequest.getPassword()))
					.roles("ROLE_USER")
					.build();
					
			userRepository.save(newUser);
			return true;
		}
		
		return false;	
	}

}
