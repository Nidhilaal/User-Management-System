package com.app.loginsystem.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.loginsystem.dto.CreateUserRequest;
import com.app.loginsystem.entity.User;
import com.app.loginsystem.repo.UserRepository;
import com.app.loginsystem.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	
	private final UserRepository userRepository;
	private final UserService userService;
	
	@GetMapping("/adminpanel")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String showAdminPanel(Model model) {
		List<User> users= userRepository.findAll();
		model.addAttribute("users",users);
		return "adminpanel";	
	}
	
	@GetMapping("/adminpanel/create")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String getCreatePage(Model model) {
		model.addAttribute("createrequest",new CreateUserRequest());
		return "createuser";
	}
	
	@PostMapping("/adminpanel/create")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String create(@ModelAttribute("createrequest")CreateUserRequest createRequest,Model model) {
		boolean result=userService.createUser(createRequest);
		if(!result && userService.getFlag()==1) {
			model.addAttribute("error","Username or Email Already Exists");
			return "/createuser";
		}else if(!result && userService.getFlag()==0){
			return "/createuser";	
		}
		else {
			return "redirect:/adminpanel";			
		}	
	}
	
	@GetMapping("/adminpanel/search")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String searchUsersByUsername(@RequestParam String searchTerm,Model model) {
		List<User>users= userRepository.findByUsername(searchTerm).stream()
                .collect(Collectors.toList());
		model.addAttribute("users",users);
		return "/adminpanel";
	}
	
	@GetMapping(value={"/adminpanel/delete/{id}","/adminpanel/adminpanel/delete/{id}"})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String deleteUser(@PathVariable ("id") Long id) {
		userRepository.deleteById(id);
		return "redirect:/adminpanel";
	}
	
	@GetMapping("/adminpanel/edit/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String editUserForm(@PathVariable("id") Long id,Model model) {
		User user=userRepository.findById(id).orElseThrow();
		model.addAttribute("user", user);
		return "updateuser";
	}
	
	@PostMapping("/adminpanel/update")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String updateUser(@ModelAttribute("user") User updatedUser) {
		userRepository.save(updatedUser);
		return "redirect:/adminpanel";
	}
	
}
