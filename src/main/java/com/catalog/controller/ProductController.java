package com.catalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.response.ProductResponse;
import com.catalog.service.IProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {  
	@Autowired
	IProductService productService;
	/***
	 * The getProductsByCategory method is to find all products based on category
	 * 
	 * @param categoryLevel
	 * @return productData
	 */
	@Operation(summary = "Get products by category based on categoryLevel", description = "API related to fetch products", tags = "Get Products")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "products fetched from database") })
	@GetMapping("/products/bycategory")
	public ResponseEntity<?> getProductsByCategory(@RequestParam String categoryLevel) {
		List<ProductResponse> productData = productService.findProductByCategory(categoryLevel);
		log.info("products list for categorylevel " + categoryLevel);
		return new ResponseEntity<>(productData, HttpStatus.OK);

	}

}
