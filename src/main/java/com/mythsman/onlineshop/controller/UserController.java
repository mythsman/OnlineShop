package com.mythsman.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.mythsman.onlineshop.model.User;
import com.mythsman.onlineshop.service.UserSecurityService;
import com.mythsman.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mythsman.onlineshop.helpers.FlashMessage;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private JavaMailSender mailSender;
    
    @Autowired
    private UserDetailsService securityService;
	
    @Autowired
    private UserService userService;
    
	@RequestMapping(value = "/remove")
	public String invalidateAccount(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		User signedIn = userService.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
		try {
			request.logout();
		} catch(ServletException e) {
			FlashMessage.createFlashMessage("alert-danger", "Couldn't finalize request. Try again later", redirectAttributes);
			return "redirect:/";
		}

		userService.disableUser(signedIn.getUsername());
		FlashMessage.createFlashMessage("alert-warning", "Your account has been removed.", redirectAttributes);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public String savePassword(Locale locale, @RequestParam("newPassword") String newPassword,
			@RequestParam("newPasswordConfirmation") String newPasswordConfirmation, Model model, 
			RedirectAttributes redirectAttributes) {
		
	    User user = 
	      userService.findByUsername((String)SecurityContextHolder.getContext()
	                                  .getAuthentication().getName());
	    List<String> errorMessages = new ArrayList<>();
	    user.setPassword(newPassword);
	    user.setPasswordConfirmation(newPasswordConfirmation);
	    userService.checkEqualityOfPasswords(user, errorMessages);
	    checkPasswordCorrectness(user, errorMessages);
	    if(errorMessages.size() > 0) {
	    	model.addAttribute("hasErrors", true);
	    	model.addAttribute("errorMessages", errorMessages);
	    	return "updatePassword";
	    }
	    FlashMessage.createFlashMessage("alert-success", "Your password has been updated.", redirectAttributes);
	    userService.updateUserPassword(user);
	    return "redirect:/";
	}	

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, RedirectAttributes redirectAttributes, 
			@RequestParam("id") long id, @RequestParam("token") String token) {
		
	    String result = ((UserSecurityService) securityService).validatePasswordResetToken(id, token);
	    if (result != null) {
	    	FlashMessage.createFlashMessage("alert-danger", "Cannot reset your password. Try again later or"
	    			+ " resend reset token on your email address.", redirectAttributes);
	        return "redirect:/login?lang=" + locale.getLanguage();
	    }
	    return "redirect:/user/updatePassword";
	}
	
	@RequestMapping(value="/updatePassword")
	public String updatePassword() {
		return "updatePassword";
	}
	
	@RequestMapping(value="/update")
	public String updateUser(Model model) {
		
		User user = userService.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
		model.addAttribute("user", user);
		return "signup";
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail,
			RedirectAttributes redirectAttributes) {
		
		User user = userService.findByEmail(userEmail);
		if (user == null) {
			return "redirect:/";
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(request.getRequestURI(), 
				request.getLocale(), token, user));
		FlashMessage.createFlashMessage("alert-success", "Your password has been changed. Check your email inbox"
				+ " for further instructions", redirectAttributes);
		return "redirect:/signin";
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

