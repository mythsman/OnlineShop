package com.mythsman.onlineshop.dao;

import com.mythsman.onlineshop.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserDao extends CrudRepository<User, Long> {
	User findByUsername(String username);
	User findByEmail(String email);
	User findByPhone(String phone);
}
