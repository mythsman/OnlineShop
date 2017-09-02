package com.darglk.onlineshop.dao;


import org.springframework.data.repository.CrudRepository;

import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.UserRole;

public interface UserRoleDao extends CrudRepository<UserRole, Long>{
	UserRole findByUser(User user);
}
