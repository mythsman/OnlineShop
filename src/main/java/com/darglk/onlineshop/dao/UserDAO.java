package com.darglk.onlineshop.dao;

import org.springframework.data.repository.CrudRepository;

import com.darglk.onlineshop.model.User;


public interface UserDao extends CrudRepository<User, Long> {
	User findByUsername(String username);
	User findByEmail(String email);
}
