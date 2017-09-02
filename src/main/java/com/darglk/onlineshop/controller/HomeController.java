package com.darglk.onlineshop.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.darglk.onlineshop.dao.RoleDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.UserRole;
import com.darglk.onlineshop.service.UserService;
import com.darglk.onlineshop.service.UserSecurityService;

@Controller
public class HomeController {	
	
	@Autowired
	private RoleDao roleDao;
	
    @Autowired
    private UserService userService;

    @Autowired
	private JavaMailSender mailSender;
    
    @Autowired
    UserDetailsService securityService;
    
	@RequestMapping("/")
	public String index() {
		return "home";
	}
	
	@RequestMapping(value="/forgotPassword", method = RequestMethod.GET)
	public String forgotPassword() {
		return "forgotpassword";
	}
	
	@RequestMapping(value="/signin", method = RequestMethod.GET)
	public String signin() {
		return "signin";
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();
		
		model.addAttribute("user", user);
		
		return "signup";
	}	
	
	@RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
	public String savePassword(Locale locale, @RequestParam("newPassword") String newPassword,
			@RequestParam("newPasswordConfirmation") String newPasswordConfirmation, Model model) {
		
	    User user = 
	      userService.findByUsername((String)SecurityContextHolder.getContext()
	                                  .getAuthentication().getName());
	    List<String> errorMessages = new ArrayList<>();
	    user.setPassword(newPassword);
	    user.setPasswordConfirmation(newPasswordConfirmation);
	    checkEqualityOfPasswords(user, errorMessages);
	    checkPasswordCorrectness(user, errorMessages);
	    if(errorMessages.size() > 0) {
	    	model.addAttribute("hasErrors", true);
	    	model.addAttribute("errorMessages", errorMessages);
	    	return "updatePassword";
	    }
	    userService.updateUserPassword(user);
	    return "redirect:/";
	}	

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, Model model, 
	  @RequestParam("id") long id, @RequestParam("token") String token) {
		
	    String result = ((UserSecurityService) securityService).validatePasswordResetToken(id, token);
	    if (result != null) {
	        model.addAttribute("message", 
	          "Password has not been reset.");
	        return "redirect:/login?lang=" + locale.getLanguage();
	    }
	    return "redirect:/user/updatePassword";
	}
	
	@RequestMapping(value="/user/updatePassword")
	public String updatePassword() {
		return "updatePassword";
	}
	
	@RequestMapping(value="/user/update")
	public String updateUser(Model model) {
		User user = (User)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
		model.addAttribute("user", user);
		return "signup";
	}
	
	@RequestMapping(value = "/user/resetPassword", 
            method = RequestMethod.POST)
	public String resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
		User user = userService.findByEmail(userEmail);
		if (user == null) {
			return "redirect:/";
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(request.getRequestURI(), 
				request.getLocale(), token, user));
		return "redirect:/signin";
}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public String signupPost(@Valid @ModelAttribute("user") User user, BindingResult bindingResult , Model model) {
		
		List<String> errorMessages = new ArrayList<>();
		User signedIn = (User)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
		if(signedIn == null) {
			validateUniqueValuesForNewUser(user, errorMessages);
		} else {
			validateUniqueValuesForExistingUser(user, errorMessages, signedIn);
		}
		
		checkEqualityOfPasswords(user, errorMessages);
		checkOtherValidationErrors(bindingResult, errorMessages);
		
		if (errorMessages.size() > 0) {
			model.addAttribute("hasErrors", true);
			model.addAttribute("errorMessages", errorMessages);
			return "signup";
		} 
		
		if(signedIn == null) {
			Set<UserRole> userRoles = new HashSet<>();
			userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));
			userService.createUser(user, userRoles);
			
		} else {
			userService.updateUserPassword(user);
			userService.save(user);
			signedIn.setUsername(user.getUsername());
		}
		return "redirect:/";
	}

	private void validateUniqueValuesForExistingUser(User user, List<String> errorMessages, User signedIn) {
		if(!user.getUsername().equals(signedIn.getUsername()))
			checkExistenceOfUsername(user, errorMessages);
		if(!user.getEmail().equals(signedIn.getEmail()))
			checkExistenceOfEmail(user, errorMessages);
		if(!user.getPhone().equals(signedIn.getPhone()))
			checkExistenceOfPhoneNumber(user, errorMessages);
	}

	private void validateUniqueValuesForNewUser(User user, List<String> errorMessages) {
		checkExistenceOfEmail(user, errorMessages);
		checkExistenceOfUsername(user, errorMessages);
		checkExistenceOfPhoneNumber(user, errorMessages);
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
	
	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/user/changePassword?id=" + 
				user.getUserId() + "&token=" + token;
		return constructEmail("Reset Password", "Hi. To reset your password, follow the link above" + " \r\n" + url, user);
	}
			 
	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom("ciprojektwimiip@gmail.com"); //TODO: UPDATE LATER
		return email;
	}
	
	private void checkPasswordCorrectness(User user, List<String> errorMessages) {
		if(!validatePassword(user.getPassword()) || !validatePassword(user.getPasswordConfirmation())) {
			errorMessages.add("One of the passwords is invalid. It should contain at least one: digit, "
			+ "upper, lower case letter, special character and its length should be in range from 6 to 60 chars");
		}		
	}

	private boolean validatePassword(String password) {
		return password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,60}");
	}
}
