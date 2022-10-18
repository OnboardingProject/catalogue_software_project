package com.catalog.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.entity.Category;
import com.catalog.request.CategoryRequest;
import com.catalog.request.CategoryUpdateRequest;
import com.catalog.service.ICategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the controller class of Category where we enter details of category
 * 
 * @author
 *
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

	@Autowired
	ICategoryService categoryService;

	/**
	 * This is the method to save categories
	 * 
	 * @param categoryRequest
	 * @return categories
	 */
	@PostMapping
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "add category to database") })
	@Operation(summary = "Adding Category", description = "API for adding the category on each levels", tags = "Add")
	public ResponseEntity<Category> saveCatalog(@Valid @RequestBody CategoryRequest categoryRequest) {
		log.info("Save Controller Entry");
		Category categories = categoryService.saveCatalog(categoryRequest);
		log.info("Save Controller Exit");
		return new ResponseEntity<Category>(categories, HttpStatus.CREATED);
	}

	/**
	 * The updateCatalog method is used to update the details of the category
	 * 
	 * @param categoryUpdateRequest
	 * @return category entity
	 */
	@PutMapping
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "update category from database") })
	@Operation(summary = "Updating the category levels based on levelId", description = "API related to updating the details of the  category", tags = "Update")
	public ResponseEntity<Category> updateCatalog(@RequestBody CategoryUpdateRequest categoryUpdateRequest) {
		Category categories = categoryService.updateCatalog(categoryUpdateRequest);
		log.info("The category details are updated for the  LevelId is " + categoryUpdateRequest.getLevelId());
		return new ResponseEntity<Category>(categories, HttpStatus.OK);
	}

	/**
	 * This is the method to fetch all categories available
	 *
	 * @return category list
	 *
	 */
	@GetMapping
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "category fetched from database") })
	@Operation(summary = "Fetching category", description = "API for displaying details of fetching categories", tags = "Get")
	public ResponseEntity<List<Category>> fetchCatalog() {
		log.info("Controller fetch category starts");
		List<Category> categoryList = categoryService.fetchCategory();
		log.info("Controller fetch category ends with response: {}", categoryList);
		return new ResponseEntity<List<Category>>(categoryList, HttpStatus.OK);
	}

}
