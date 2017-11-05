package com.mythsman.onlineshop.dao;


import com.mythsman.onlineshop.model.User;
import org.springframework.data.repository.CrudRepository;

import com.mythsman.onlineshop.security.UserRole;

public interface UserRoleDao extends CrudRepository<UserRole, Long>{
	UserRole findByUser(User user);
}
