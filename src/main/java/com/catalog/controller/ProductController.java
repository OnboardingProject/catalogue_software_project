package com.catalog.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.entity.Product;
import com.catalog.request.ProductRequest;
import com.catalog.request.ProductUpdateRequest;
import com.catalog.response.ProductResponse;
import com.catalog.service.IProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	IProductService productService;
	@Operation(summary = "Add products ", description = "API related to add products", tags = "Add")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "add product from database") })
	@PostMapping
	public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest) {
		Product product = productService.addProduct(productRequest);

		return new ResponseEntity<Product>(product, HttpStatus.CREATED);

	}

	/**
	 * This method will update a product and also validate incoming request
	 * 
	 * @param UpdateDTO
	 * @return response entity representation of UpdateDTO
	 */
	@Operation(summary = "Update products  based on product id", description = "API related to update products", tags = "Update")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "update product from database") })
	@PutMapping
	public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
		Product productUpdate = productService.updateProduct(productUpdateRequest);

		if (ObjectUtils.isEmpty(productUpdate)) {
			return new ResponseEntity<>("No updated data", HttpStatus.CONFLICT);
		}

		return new ResponseEntity<Product>(productUpdate, HttpStatus.OK);

	}

	/**
	 * Method to add the get product with letter/word of name
	 * 
	 * @param product name
	 * @return product
	 */
	@Operation(summary = "Search products  based on product name", description = "API related to search products", tags = "Get")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "search product from database") })
	@GetMapping("/search/name")
	public ResponseEntity<List<Product>> searchByName(@RequestParam(required = true) String productName) {
		log.info("ProductController searchByName call started with {}", productName);
		List<Product> products = productService.searchByName(productName);

		if (!CollectionUtils.isEmpty(products)) {
			log.info("ProductController searchByName call ended with {}", products);
			return new ResponseEntity<>(products, HttpStatus.OK);
		} else
			log.info("No product exist with the name {}", productName);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/***
	 * The getProductsByCategory method is to find all products based on category
	 * 
	 * @param categoryLevel
	 * @return productData
	 */
	@Operation(summary = "Get products by category based on categoryLevel", description = "API related to fetch products", tags = "Get")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "products fetched from database") })
	@GetMapping("/search/category")
	public ResponseEntity<?> getProductsByCategory(@RequestParam String categoryLevel) {
		List<ProductResponse> productData = productService.findProductByCategory(categoryLevel);
		log.info("products list for categorylevel " + categoryLevel);
		return new ResponseEntity<>(productData, HttpStatus.OK);

	}

}
