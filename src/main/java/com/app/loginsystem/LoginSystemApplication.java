package com.app.loginsystem;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.loginsystem.entity.User;
import com.app.loginsystem.repo.UserRepository;

@SpringBootApplication
public class LoginSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginSystemApplication.class, args);
		
	}
	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository,PasswordEncoder encoder) {
		return args->{
			
			User user=User.builder().username("user")
					.email("user@gmail.com")
					.password(encoder.encode("password1"))
					.roles("ROLE_USER")
					.build();
			
			User admin=User.builder().username("admin")
					.email("admin@gmail.com")
					.password(encoder.encode("password2"))
					.roles("ROLE_USER,ROLE_ADMIN")
					.build();
			
			userRepository.save(user);
			userRepository.save(admin);
			
		};
	}

}
