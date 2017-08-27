package com.darglk.onlineshop.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.darglk.onlineshop.dao.RoleDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.UserRole;
import com.darglk.onlineshop.service.UserService;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

	
    
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profilePost(@ModelAttribute("user") User newUser, Model model) {
        User user = userService.findByUsername(newUser.getUsername());
        user.setUsername(newUser.getUsername());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPhone(newUser.getPhone());

        model.addAttribute("user", user);

        userService.saveUser(user);

        return "redirect:/";
    }
      
}

