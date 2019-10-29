package com.packt.webstore.controller;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.packt.webstore.domain.Product;
import com.packt.webstore.service.ProductService;

@Controller
@RequestMapping("/market")
public class ProductController {

	@Autowired
	 private ProductService productService;
	
	@RequestMapping("/products")
	public String list(Model model) {
		
	model.addAttribute("products",
			productService.getAllProducts());
	
	return "products";
	}
	
	@RequestMapping("/update/stock")
	 public String updateStock(Model model) {
		productService.updateAllStock();
		return "redirect:/market/products";
	 }
	@RequestMapping("/products/{category}")
	public String getProductsByCategory(Model model, @PathVariable("category") String category) {
		model.addAttribute("products", productService.getProductsByCategory(category));
		return "products";
	}
	
	@RequestMapping("/products/filter/{params}")
	public String getProductsByFilter(@MatrixVariable(pathVar="params")
				Map<String,List<String>> filterParams, Model model) {
	
	model.addAttribute("products", productService.getProductsByFilter(filterParams));
	return "products";
	
	}
	
	
	@RequestMapping("/product")
	public String getProductById(@RequestParam("id") String productId, Model model) {
			model.addAttribute("product",productService.getProductById(productId));
			return "product";
	}
	
	@RequestMapping("/products/{category}/{price}")
	public String filterProducts(@PathVariable String category, @MatrixVariable Map<String, List<BigDecimal>> price,
				 @RequestParam String brand , Model model) {
		model.addAttribute("products", productService.filterProducts(category, price, brand));
		return "products";
	}
	
	// add product .. add object to form to bining
	@RequestMapping(value = "/products/add")
	public String getAddNewProductForm(Model model) {
		Product newProduct = new Product();
		model.addAttribute("newProduct", newProduct);
		return "addProduct";
	}
	
	
	@RequestMapping(value = "/products/add", method = RequestMethod.POST)
	public String processAddNewProductForm(@ModelAttribute("newProduct") Product newProduct) {
		productService.addProduct(newProduct);
		return "redirect:/market/products";
	}
}
