package com.packt.webstore.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packt.webstore.domain.Product;
import com.packt.webstore.domain.repository.ProductRepository;
import com.packt.webstore.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public void updateAllStock() {
		List<Product> allProducts =
				 productRepository.getAllProducts();
		allProducts.stream().forEach(product -> {
			if(product.getUnitsInStock() < 500)
				productRepository.updateStock(product.getProductId(), 
						product.getUnitsInStock()+1000);
		 });

	}

	@Override
	public List<Product> getAllProducts() {
		return this.productRepository.getAllProducts();
	}
	
	@Override
	public List<Product> getProductsByCategory(String category) {
		return productRepository.getProductsByCategory(category);
	}

	@Override
	public List<Product> getProductsByFilter(Map<String, List<String>> filterParams) {
		return productRepository.getProductsByFilter(filterParams);
	}
	
	@Override
	public Product getProductById(String productID) {
		return productRepository.getProductById(productID);
	}
	
	@Override
	public List<Product> filterProducts(String category, Map<String, List<BigDecimal>> price, String brand) {
		return productRepository.filterProducts(category, price, brand);
	}

	@Override
	public void addProduct(Product product) {
		productRepository.addProduct(product);
		
	}

}
