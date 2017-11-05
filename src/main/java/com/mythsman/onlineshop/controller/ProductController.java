package com.mythsman.onlineshop.controller;


import com.mythsman.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mythsman.onlineshop.model.Product;
import com.mythsman.onlineshop.service.CategoryService;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
	public String getProductById(@PathVariable("id") Long id, Model model) {
		Product product = productService.findById(id);
		if (product.getQuantity() <= 0L) {
			return "redirect:/";
		}
		loadCategories(model);
		model.addAttribute("product", product);
		return "showProduct";
	}
	
	@GetMapping("/list")
	public String listProducts(Model model, @RequestParam(name="page", defaultValue="0", required=false) Integer page,
			@RequestParam(name="searchTerm", defaultValue="", required=false) String searchTerm) {
		Pageable pageable = new PageRequest(page, 6);
		Page<Product> products = productService.getProductsByTerm(searchTerm, pageable);
		loadCategories(model);
		
		model.addAttribute("showPagination", true);
		model.addAttribute("products", products);
		model.addAttribute("page", page);
		model.addAttribute("searchTerm", searchTerm);
		
		return "home";
	}
	
	@RequestMapping(value = "/list/category/{id}", method = RequestMethod.GET)
	public String loadProductsByCategory(@PathVariable("id") Integer id, Model model, 
			@RequestParam(name="page", defaultValue="0", required=false) Integer page) {
		Pageable pageable = new PageRequest(page, 6);
		Page<Product> products = productService.getProductsByCategoryId(id, pageable);
		
		loadCategories(model);
		model.addAttribute("showPagination", true);
		model.addAttribute("products", products);
		model.addAttribute("page", page);
		return "home";
	}	
	
	private void loadCategories(Model model) {
		model.addAttribute("categories", categoryService.getCategories());
	}
}
