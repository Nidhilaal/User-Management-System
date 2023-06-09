package com.app.loginsystem.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.loginsystem.dto.CreateUserRequest;
import com.app.loginsystem.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final UserService userService;
	
	@GetMapping("/")
	public String getHomePage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/adminpanel";
        } 
        return "home";        
	}
	
//	These are the mappings to stay in the same page if any invalid urls are given.
//	The more optimized way is to create a custom error page to handle all the invalid urls.
	
//	@RequestMapping("/**")
//    public String handleInvalidUrls() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if (isAdmin) {
//            return "redirect:/adminpanel";
//        } else {
//            return "redirect:/";
//        }
//    }
	
//	@RequestMapping("/adminpanel/**")
//	public String handleInvalidUrlsInCreate() {
//		return "redirect:/adminpanel/create";
//	}
	
	@GetMapping("/login")
	public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	        if (error != null) {
	            model.addAttribute("error", "Invalid username or password");
	        }
	        return "login";
	    }
	    return "redirect:/";
	}
	
	@GetMapping("/signup")
	public String getSignUpPage(Model model) {
		model.addAttribute("signuprequest",new CreateUserRequest());
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute("signuprequest")CreateUserRequest signUpRequest,Model model) {
		boolean result=userService.createUser(signUpRequest);
		if(!result) {
			model.addAttribute("error", "Username or email already exists");
			return "/signup";
		}else {
			return "redirect:/login";		
		}
	} 
	
}
