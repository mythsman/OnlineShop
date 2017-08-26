package com.darglk.onlineshop.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.darglk.onlineshop.model.User;

@Controller
@RequestMapping("/users")
public class UsersController {

	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/signup")
	public String onSignUp(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
		
		return "redirect:/";
	}
}
