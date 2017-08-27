package com.darglk.onlineshop.controller;

import java.util.HashSet;
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
		if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
			
			if (userService.checkEmailExists(user.getEmail())) {
				model.addAttribute("emailExists", true);
			}
			
			if (userService.checkUsernameExists(user.getUsername())) {
				model.addAttribute("usernameExists", true);
			}
			
			return "signup";
		} else {
			Set<UserRole> userRoles = new HashSet<>();
			userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));
			userService.createUser(user, userRoles);
			
			return "redirect:/";
		}
	}
}
