package com.app.loginsystem.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

public class Errorcontroller {
	@GetMapping("/error")
    public String handleError() {
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "redirect:/error";
    }
}
