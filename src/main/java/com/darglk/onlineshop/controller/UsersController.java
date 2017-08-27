package com.darglk.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.service.UserService;

@Controller
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/signup")
	public String onSignUp(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
		
		model.addAttribute("passwordsNotEqual", false);		
		List<String> errorMessages = new ArrayList<>();
		User updatingUser = userService.getUser(user.getId());
		checkIfPasswordsAreEqual(user, errorMessages);
		if (updatingUser == null) {			
			
			checkIfUserNameExists(user, errorMessages);
			checkIfEmailExists(user, errorMessages);
		} else {
			
			if (!updatingUser.getUserName().equals(user.getUserName())) { checkIfUserNameExists(user, errorMessages); }
			if (!updatingUser.getEmailAddress().equals(user.getEmailAddress())) { checkIfEmailExists(user, errorMessages); }			
		}
		
		if (bindingResult.hasErrors() || !errorMessages.isEmpty()) {

			model.addAttribute("formErrors", errorMessages);
			model.addAttribute("user", user);
			
			return "signup";
		}
		
		setAdditionalUserInfo(user);		
		return "redirect:/";
	}
	
	private void checkIfEmailExists(User user, List<String> errorMessages) {
		if (emailExists(user.getEmailAddress())) { errorMessages.add("Email exists in database."); }
	}

	private void checkIfUserNameExists(User user, List<String> errorMessages) {
		if (usernameExists(user.getUserName())) { errorMessages.add("Username exists in database."); }
	}

	private void checkIfPasswordsAreEqual(User user, List<String> errorMessages) {
		if (!passwordsEqual(user.getPassword(), user.getPasswordConfirmation())) { errorMessages.add("Passwords are not equal."); }
	}

	private void setAdditionalUserInfo(User user) {
		user.setEnabled(true);
		long userId = user.getId();
		
		userService.saveUser(user);
		if (userId == 0) {			
			List<User> userList = userService.getByUsername(user.getUserName());
			if(!userList.isEmpty()) {
				user = userList.get(0);
				userService.updateUserRole(user);
				//TODO: - LATER sendWelcomeMailMessage(user.getEmailAddress());
			}
		}		
	}
	
	private boolean passwordsEqual(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}
	
	private boolean usernameExists(String username) {
		return !userService.getByUsername(username).isEmpty();
	}
	
	private boolean emailExists(String email) {
		return !userService.getByEmail(email).isEmpty();
	}
}
