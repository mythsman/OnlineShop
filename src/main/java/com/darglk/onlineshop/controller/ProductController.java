package com.darglk.onlineshop.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.Product;
import com.darglk.onlineshop.service.CartService;
import com.darglk.onlineshop.service.CategoryService;
import com.darglk.onlineshop.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
	public String getProductById(@PathVariable("id") Long id, Model model) {
		Product product = productService.findById(id);
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
	
	@RequestMapping(value = "/add_to_cart", method = RequestMethod.POST)
	public String addToCart(@RequestParam("product_id") Long id, Model model, HttpServletRequest httpRequest) {
		Cart cart = null;
		if(httpRequest.getSession().getAttribute("cart_id") == null) {
			cart = cartService.createCart();
			httpRequest.getSession().setAttribute("cart_id", cart.getId());
		} else {
			cart = cartService.findCart((Long)httpRequest.getSession().getAttribute("cart_id"));
		}
		cart = cartService.addItemToCart(cart.getId(), id);
		return "redirect:/cart/show";
	}
	
	private void loadCategories(Model model) {
		model.addAttribute("categories", categoryService.getCategories());
	}
}
