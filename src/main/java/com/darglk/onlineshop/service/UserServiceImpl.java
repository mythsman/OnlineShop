package com.darglk.onlineshop.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.darglk.onlineshop.dao.UserDAO;
import com.darglk.onlineshop.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	@Transactional
	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDAO.saveUser(user);
	}
	
	@Override
	@Transactional
	public User getUser(Long userId) {
		return userDAO.getUser(userId);
	}
	
	@Override
	@Transactional
	public void deleteUser(int userId) {
		userDAO.deleteUser(userId);
	}
	
	@Override
	@Transactional
	public List<User> getByUsername(String username) {
		return userDAO.getByUsername(username);
	}

	@Override
	@Transactional
	public List<User> getByEmail(String email) {
		return userDAO.getByEmail(email);
	}
}
