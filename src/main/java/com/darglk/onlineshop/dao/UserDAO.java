package com.darglk.onlineshop.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.darglk.onlineshop.model.User;

@Repository
public interface UserDAO {

	public void saveUser(User user);

	User getUser(Long userId);

	void deleteUser(int userId);

	List<User> getByUsername(String username);

	List<User> getByEmail(String email);
}
