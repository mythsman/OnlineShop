package com.mythsman.onlineshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mythsman.onlineshop.model.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {

}
