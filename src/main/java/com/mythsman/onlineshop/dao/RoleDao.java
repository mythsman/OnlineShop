package com.mythsman.onlineshop.dao;

import com.mythsman.onlineshop.security.Role;
import org.springframework.data.repository.CrudRepository;


public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
