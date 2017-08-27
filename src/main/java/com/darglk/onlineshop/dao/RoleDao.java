package com.darglk.onlineshop.dao;

import org.springframework.data.repository.CrudRepository;

import com.darglk.onlineshop.security.Role;


public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
