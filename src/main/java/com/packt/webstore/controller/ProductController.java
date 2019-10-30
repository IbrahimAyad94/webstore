package com.packt.webstore.controller;


import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.packt.webstore.domain.Product;
import com.packt.webstore.exception.NoProductsFoundUnderCategoryException;
import com.packt.webstore.exception.ProductNotFoundException;
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
		List<Product> products = productService.getProductsByCategory(category);
		model.addAttribute("products", productService.getProductsByCategory(category));
		
		// custom exception if products not found
		if (products == null || products.isEmpty()) 
			throw new NoProductsFoundUnderCategoryException();
		
		
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
	public String processAddNewProductForm(@ModelAttribute("newProduct") Product newProduct
			,  BindingResult result, HttpServletRequest request) {
		
		// error in binding 
		 String[] suppressedFields = result.getSuppressedFields();
		 if (suppressedFields.length > 0) {
		 throw new RuntimeException("Attempting to bind disallowed fields: " + 
				 StringUtils.arrayToCommaDelimitedString(suppressedFields));
		 }

		 // image upload
		 MultipartFile productImage = newProduct.getProductImage();
		 String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		 if (productImage!=null && !productImage.isEmpty()) {
			 try {
				 productImage.transferTo(new
				 File(rootDirectory+"/resources/images/"+ newProduct.getProductId() + ".jpg"));
			 } catch (Exception e) {
				 throw new RuntimeException("Product Image saving failed", e);
			 }
		 }
		
		 MultipartFile productPDF = newProduct.getProductPDF();
		productService.addProduct(newProduct);
		if(productPDF != null && !productPDF.isEmpty()) {
			try {
				productPDF.transferTo(new
				 File(rootDirectory+"/resources/pdf/"+ newProduct.getProductId() + ".pdf"));
			 } catch (Exception e) {
				 throw new RuntimeException("Product Image saving failed", e);
			 }
		}
		return "redirect:/market/products";
	}
	
	// white list to properties in model 
	@InitBinder
	 public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("productId", "name", "unitPrice", "description", 
				"manufacturer", "category", "unitsInStock", "condition", "productImage", "productPDF");
	 }
	
	// handel customer error ( throw form dao, handel it from controller to show view )
	/*@ExceptionHandler(ProductNotFoundException.class)
	public ModelAndView handleError(HttpServletRequest req, ProductNotFoundException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("invalidProductId", exception.getProductId());
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL()+"?"+req.getQueryString());
		mav.setViewName("productNotFound");
		return mav;
	}*/
	
	@ExceptionHandler(ProductNotFoundException.class)
	public String handleError(Model model, HttpServletRequest req, ProductNotFoundException exception) {
		model.addAttribute("invalidProductId", exception.getProductId());
		model.addAttribute("exception", exception);
		model.addAttribute("url", req.getRequestURL()+"?"+req.getQueryString());
		model.addAttribute("productNotFound");
		return "productNotFound";
	}
}
