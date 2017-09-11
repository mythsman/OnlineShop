package com.darglk.onlineshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.darglk.onlineshop.model.Cart;

public interface CartDao extends JpaRepository<Cart, Long> {

}
