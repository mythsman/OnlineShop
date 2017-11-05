package com.mythsman.onlineshop.dao;

import com.mythsman.onlineshop.security.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;


public interface PasswordTokenDao extends CrudRepository<PasswordResetToken, Integer>{

	PasswordResetToken findByToken(String token);
}
