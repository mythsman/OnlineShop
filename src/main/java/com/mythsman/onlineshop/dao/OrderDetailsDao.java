package com.mythsman.onlineshop.dao;

import com.mythsman.onlineshop.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsDao extends JpaRepository<OrderDetails, Long> {

}
