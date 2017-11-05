package com.mythsman.onlineshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mythsman.onlineshop.model.Cart;

public interface CartDao extends JpaRepository<Cart, Long> {

}
