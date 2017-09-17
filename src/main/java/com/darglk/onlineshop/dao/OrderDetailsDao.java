package com.darglk.onlineshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.darglk.onlineshop.model.OrderDetails;

public interface OrderDetailsDao extends JpaRepository<OrderDetails, Long> {

}
