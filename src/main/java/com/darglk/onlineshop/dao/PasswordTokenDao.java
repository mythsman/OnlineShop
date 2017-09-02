package com.darglk.onlineshop.dao;

import org.springframework.data.repository.CrudRepository;

import com.darglk.onlineshop.security.PasswordResetToken;


public interface PasswordTokenDao extends CrudRepository<PasswordResetToken, Integer>{

	PasswordResetToken findByToken(String token);

}
