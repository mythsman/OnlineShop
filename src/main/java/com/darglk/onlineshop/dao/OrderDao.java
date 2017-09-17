package com.darglk.onlineshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.darglk.onlineshop.model.Order;

public interface OrderDao extends JpaRepository<Order, Long> {

}
