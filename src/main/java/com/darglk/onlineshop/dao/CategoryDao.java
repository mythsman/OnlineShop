package com.darglk.onlineshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.darglk.onlineshop.model.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {

}
