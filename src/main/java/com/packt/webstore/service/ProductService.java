package com.packt.webstore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.packt.webstore.domain.Product;

public interface ProductService {

	List<Product> getAllProducts();

	void updateAllStock();
	 
	List<Product> getProductsByCategory(String category);
	 
	List<Product> getProductsByFilter(Map<String,List<String>>filterParams);

	Product getProductById(String productID);

	List<Product> filterProducts(String category, Map<String, List<BigDecimal>> price, String brand);
	
	void addProduct(Product product);
}
