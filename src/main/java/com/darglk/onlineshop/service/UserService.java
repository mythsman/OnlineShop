package com.darglk.onlineshop.service;

import java.util.List;

import com.darglk.onlineshop.model.User;

public interface UserService {

	void saveUser(User user);

	User getUser(Long userId);

	void deleteUser(int userId);

	List<User> getByUsername(String username);

	List<User> getByEmail(String email);

}
