package com.darglk.onlineshop.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.darglk.onlineshop.dao.RoleDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.UserRole;
import com.darglk.onlineshop.service.UserService;

@Controller
public class HomeController {	
	
	@Autowired
	private RoleDao roleDao;
	
    @Autowired
    private UserService userService;
    
	@RequestMapping("/")
	public String index() {
		return "home";
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();
		
		model.addAttribute("user", user);
		
		return "signup";
	}	
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public String signupPost(@Valid @ModelAttribute("user") User user, BindingResult bindingResult , Model model) {
		
		List<String> errorMessages = new ArrayList<>();
		
		checkExistenceOfEmail(user, errorMessages);
		checkExistenceOfUsername(user, errorMessages);
		checkExistenceOfPhoneNumber(user, errorMessages);
		checkEqualityOfPasswords(user, errorMessages);
		checkOtherValidationErrors(bindingResult, errorMessages);
		
		if (errorMessages.size() > 0) {
			model.addAttribute("hasErrors", true);
			model.addAttribute("errorMessages", errorMessages);
			return "signup";
		} else {
			Set<UserRole> userRoles = new HashSet<>();
			userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));
			userService.createUser(user, userRoles);
			return "redirect:/";
		}
	}

	private void checkOtherValidationErrors(BindingResult bindingResult, List<String> errorMessages) {
		if(bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> {
				errorMessages.add(error.getDefaultMessage());
			});
		}
	}

	private void checkEqualityOfPasswords(User user, List<String> errorMessages) {
		if(!checkPasswordsAreEqual(user.getPassword(), user.getPasswordConfirmation())) {
			errorMessages.add("Passwords are not equal.");
		}
	}

	private void checkExistenceOfPhoneNumber(User user, List<String> errorMessages) {
		if(userService.checkPhoneNumberExists(user.getPhone())) {
			errorMessages.add("Phone number exists in database.");
		}
	}

	private void checkExistenceOfUsername(User user, List<String> errorMessages) {
		if (userService.checkUsernameExists(user.getUsername())) {
			errorMessages.add("Username exists in database.");
		}
	}

	private void checkExistenceOfEmail(User user, List<String> errorMessages) {
		if (userService.checkEmailExists(user.getEmail())) {
			errorMessages.add("Email exists in database.");
		}
	}

	private boolean checkPasswordsAreEqual(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}
}
